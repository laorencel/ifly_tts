package com.laorencel.iflytts;

import io.flutter.Log;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MethodCallHandlerImpl implements MethodChannel.MethodCallHandler {

    private static String TAG = MethodCallHandlerImpl.class.getSimpleName();

    private MethodChannel channel;
    private final Iflytts iflytts;

    public MethodCallHandlerImpl(Iflytts iflytts) {
        this.iflytts = iflytts;
    }

    void start(MethodChannel channel) {
        this.channel = channel;
        if (null != channel) {
            channel.setMethodCallHandler(this);
        }
    }

    void stop() {
        if (null != channel) {
            channel.setMethodCallHandler(null);
            channel = null;
        }
    }


    static final String METHOD_CALL_INITWITHAPPID = "initWithAppId";
    static final String METHOD_CALL_SETPARAMETER = "setParameter";

    ////语音识别
    static final String METHOD_CALL_STARTLISTENING = "startListening";
    //  static final String METHOD_CALL_WRITEAUDIO = "writeAudio";
    static final String METHOD_CALL_STOPLISTENING = "stopListening";
    //  static final String METHOD_CALL_ISLISTENING = "isListening";
    static final String METHOD_CALL_CANCELLISTENING = "cancelListening";

    ////语音合成
    static final String METHOD_CALL_STARTSPEAKING = "startSpeaking";
    static final String METHOD_CALL_PAUSESPEAKING = "pauseSpeaking";
    static final String METHOD_CALL_RESUMESPEAKING = "resumeSpeaking";
    static final String METHOD_CALL_STOPSPEAKING = "stopSpeaking";
    static final String METHOD_CALL_ISSPEAKING = "isSpeaking";


    static final String METHOD_CALL_DISPOSE = "dispose";

    @Override
    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
        Log.e(TAG, call.method);
        switch (call.method) {
            case METHOD_CALL_INITWITHAPPID:
                iflytts.initWithAppId(call, result);  // recongnnizer & synthesizer
                break;
            case METHOD_CALL_SETPARAMETER:  // recongnnizer & synthesizer
                iflytts.setParameter(call, result);
                break;
            case METHOD_CALL_DISPOSE: // recongnnizer & synthesizer
                iflytts.dispose(call, result);
                break;
            case METHOD_CALL_STARTLISTENING:
                iflytts.startListening(call, result);
                break;
            case METHOD_CALL_STOPLISTENING:
                iflytts.stopListening(call, result);
                break;
            case METHOD_CALL_CANCELLISTENING:
                iflytts.cancelListening(call, result);
                break;
            case METHOD_CALL_STARTSPEAKING:
                iflytts.startSpeaking(call, result);
                break;
            case METHOD_CALL_PAUSESPEAKING:
                iflytts.pauseSpeaking(call, result);
                break;
            case METHOD_CALL_RESUMESPEAKING:
                iflytts.resumeSpeaking(call, result);
                break;
            case METHOD_CALL_STOPSPEAKING:
                iflytts.stopSpeaking(call, result);
                break;

            default:
                throw new IllegalArgumentException("Unknown method " + call.method);
        }
    }
}
