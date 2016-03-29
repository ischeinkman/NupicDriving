package com.ilanscheinkman.nupicdriving.Model;

import com.github.pires.obd.commands.ObdCommand;
import com.github.pires.obd.commands.SpeedCommand;
import com.github.pires.obd.commands.engine.RPMCommand;
import com.github.pires.obd.commands.engine.ThrottlePositionCommand;
import com.github.pires.obd.commands.fuel.ConsumptionRateCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import rx.Observable;
import rx.Subscriber;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * A class for running and emitting readings from a connects OBD device.
 * Created by ilan on 2/8/16.
 */
public class ObdManager {

    /**
     * How long to wait between readings.
     */
    public static final long READING_DELAY = 1000;

    /**
     * The fields we are reading from the device.
     */
    public static final ObdCommand[] FIELDS = new ObdCommand[]{
            new SpeedCommand(),
            new RPMCommand(),
            new ThrottlePositionCommand(),
            new ConsumptionRateCommand()
    };

    private boolean isClosed;
    private InputStream inputStream;
    private OutputStream outputStream;

    /**
     * Constructs a new ObdManager.
     * @param obdInput The inputstream from the bluetooth socket to the OBD device
     * @param obdOutput The outputstream from the bluetooth socket to the OBD device
     */
    public ObdManager(InputStream obdInput, OutputStream obdOutput){
        inputStream = obdInput;
        outputStream = obdOutput;
        isClosed = false;
    }

    /**
     * Starts reading from the OBD device. The program will
     * run the commands listed in {@link #FIELDS} and emit {@link CarReading}s
     * every {@link #READING_DELAY} milliseconds until {@link #stop()} is called.
     * The readings are also saved in the database automatically.
     * @return the observable where new readings will be omitted
     */
    public Observable<CarReading> start(){
        isClosed = false;
        ConnectableObservable<CarReading> o = Observable.create(new Observable.OnSubscribe<CarReading>() {
            @Override
            public void call(Subscriber<? super CarReading> subscriber) {
                while (!isClosed){
                    try {
                        subscriber.onNext(createReading());
                        Thread.sleep(READING_DELAY);
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).publish();
        o.connect();
        o.subscribe();
        return o;
    }
    /**
     * Stops the stream of car readings.
     */
    public void stop(){
        isClosed = true;
    }

    private CarReading createReading() throws IOException, InterruptedException {

        //Create the model
        CarReading reading = new CarReading();
        reading.setTimeStamp(System.currentTimeMillis());

        //Get the readings
        for (ObdCommand command : FIELDS){
            command.run(inputStream,outputStream);
            reading.putReading(command.getName(), Double.parseDouble(command.getResult()));
        }

        return reading;
    }



}
