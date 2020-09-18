package com.laorencel.iflytts;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.flutter.Log;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;


public class Iflytts {


    // recongnnizer
    static final String METHOD_CALL_ONBEGINOFSPEECH = "onBeginOfSpeech";
    static final String METHOD_CALL_ONENDOFSPEECH = "onEndOfSpeech";
    static final String METHOD_CALL_ONRESULTS = "onResults";
    static final String METHOD_CALL_ONVOLUMECHANGED = "onVolumeChanged";

    // synthesizer
    static final String METHOD_CALL_ONSPEAKBEGIN = "startSpeaking";
    static final String METHOD_CALL_ONBUFFERPROGRESS = "onBufferProgress";
    static final String METHOD_CALL_ONSPEAKPAUSED = "onSpeakPaused";
    static final String METHOD_CALL_ONSPEAKRESUMED = "onSpeakResumed";
    static final String METHOD_CALL_ONSPEAKPROGRESS = "onSpeakProgress";

    // recongnnizer & synthesizer
    static final String METHOD_CALL_ONCOMPLETED = "onCompleted";

    private static String TAG = Iflytts.class.getSimpleName();
    private final Context applicationContext;


    private MethodChannel channel;

    private SpeechRecognizer recognizer;
    private SpeechSynthesizer synthesizer;
    private String filePath;
    private StringBuilder resultBuilder = new StringBuilder();

    public Iflytts(Context applicationContext, MethodChannel channel) {
        this.applicationContext = applicationContext;
        this.channel = channel;
    }

    public void initWithAppId(MethodCall call, MethodChannel.Result result) {
        Log.e(TAG, call.method);

        SpeechUtility.createUtility(applicationContext.getApplicationContext(), SpeechConstant.APPID + "=" + call.arguments);
        Setting.setLocationEnable(false);
        recognizer = SpeechRecognizer.createRecognizer(applicationContext.getApplicationContext(), new InitListener() {
            @Override
            public void onInit(int code) {
                if (code != ErrorCode.SUCCESS) {
                    Log.e(TAG, "Failed to init SpeechRecognizer  Code: " + code);
                } else Log.e(TAG, "Init SpeechRecognizer Success");
            }
        });

        synthesizer = SpeechSynthesizer.createSynthesizer(applicationContext.getApplicationContext(), new InitListener() {
            @Override
            public void onInit(int i) {
                if (i != ErrorCode.SUCCESS) {
                    Log.e(TAG, "Failed to init SpeechSynthesizer Code: " + i);
                } else Log.e(TAG, "Init SpeechSynthesizer Success");
            }
        });

        result.success(null);
    }

    public void setParameter(MethodCall call, MethodChannel.Result result) {
        Log.e(TAG, "setParameter");
        if (recognizer == null) {
            Log.e(TAG, "recongnizer为null");
        } else {
            try {
                Map<String, String> map = (Map<String, String>) call.arguments;
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (entry.getKey().equals(SpeechConstant.ASR_AUDIO_PATH)) {
                        filePath = Environment.getExternalStorageDirectory() + "/msc/" + entry.getValue();
                        recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, filePath);
                    } else {
                        recognizer.setParameter(entry.getKey(), entry.getValue());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (synthesizer == null) {
            Log.e(TAG, "synthesizer 为 null");
        } else {
            try {
                Map<String, String> map = (Map<String, String>) call.arguments;
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (entry.getKey().equals(SpeechConstant.ASR_AUDIO_PATH)) {
//                        filePath = Environment.getExternalStorageDirectory() + "/msc/" + entry.getValue();
//                        recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, filePath);
                    } else {
                        synthesizer.setParameter(entry.getKey(), entry.getValue());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        result.success(null);
    }

    public void startListening(MethodCall call, MethodChannel.Result result) {


        if (recognizer == null) {
            Log.e(TAG, "SpeechRecognizer hasn't been init");
            result.error("", "SpeechRecognizer hasn't been init", "SpeechRecognizer is null");
        } else {
            int code = recognizer.startListening(mRecognizerListener);
            if (code != ErrorCode.SUCCESS)
                Log.e(TAG, "SpeechRecognizer => startListening error: " + code);
        }
        result.success(null);
    }


    public void stopListening(MethodCall call, MethodChannel.Result result) {


        if (recognizer == null) {
            Log.e(TAG, "SpeechRecognizer hasn't been init");
            result.error("", "SpeechRecognizer hasn't been init", "SpeechRecognizer is null");
        } else {
            recognizer.stopListening();
            result.success(null);
        }
    }

    public void cancelListening(MethodCall call, MethodChannel.Result result) {


        if (recognizer == null) {
            Log.e(TAG, "SpeechRecognizer hasn't been init");
            result.error("", "SpeechRecognizer hasn't been init", "SpeechRecognizer is null");
        } else recognizer.cancel();
        result.success(null);
    }

    public void dispose(MethodCall call, MethodChannel.Result result) {


        if (recognizer == null) {
            Log.e(TAG, "SpeechRecognizer hasn't been init");
            result.error("", "SpeechRecognizer hasn't been init", "SpeechRecognizer is null");
        } else {
            if (recognizer.isListening()) recognizer.cancel();
            recognizer.destroy();
            recognizer = null;
        }

        if (synthesizer == null) {
            Log.e(TAG, "SpeechSynthesizer hasn't been init");
            result.error("", "SpeechSynthesizer hasn't been init", "SpeechSynthesizer is null");
        } else {
            if (synthesizer.isSpeaking()) synthesizer.stopSpeaking();
            synthesizer.destroy();
            synthesizer = null;
        }

        result.success(null);
    }

    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        @Override
        public void onBeginOfSpeech() {
            Log.d(TAG, "onBeginOfSpeech()");
            channel.invokeMethod(METHOD_CALL_ONBEGINOFSPEECH, null);
        }

        @Override
        public void onError(SpeechError error) {
            Log.d(TAG, "onError():" + error.getPlainDescription(true));

            HashMap errorInfo = new HashMap();
            errorInfo.put("code", error.getErrorCode());
            errorInfo.put("desc", error.getErrorDescription());
            ArrayList<Object> arguments = new ArrayList<>();
            arguments.add(errorInfo);
            arguments.add(filePath);
            channel.invokeMethod(METHOD_CALL_ONCOMPLETED, arguments);
        }

        @Override
        public void onEndOfSpeech() {
            Log.d(TAG, "onEndOfSpeech()");
            channel.invokeMethod(METHOD_CALL_ONENDOFSPEECH, null);
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {

            String result = resultBuilder.append(results.getResultString()).toString();
            Log.d(TAG, "onResult():" + result);

            ArrayList<Object> arguments = new ArrayList<>();
            arguments.add(result);
            arguments.add(isLast);
            channel.invokeMethod(METHOD_CALL_ONRESULTS, arguments);

            if (isLast) {
                resultBuilder.delete(0, resultBuilder.length());

                ArrayList<Object> args = new ArrayList<>();
                arguments.add(null);
                arguments.add(filePath);
                channel.invokeMethod(METHOD_CALL_ONCOMPLETED, args);
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            channel.invokeMethod(METHOD_CALL_ONVOLUMECHANGED, volume);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };


    public void startSpeaking(MethodCall call, MethodChannel.Result result) {


        if (synthesizer == null) {
            Log.e(TAG, "SpeechSynthesize hasn't been init");
            result.error("", "SpeechSynthesize hasn't been init", "SpeechSynthesize is null");
        } else {
            int code = synthesizer.startSpeaking(call.arguments.toString(), mSynthesizerListener);
            if (code != ErrorCode.SUCCESS)
                Log.e(TAG, "SpeechSynthesize => startSpeaking error: " + code);
        }
        result.success(null);
    }

    public void pauseSpeaking(MethodCall call, MethodChannel.Result result) {


        if (synthesizer == null) {
            Log.e(TAG, "SpeechSynthesize hasn't been init");
            result.error("", "SpeechSynthesize hasn't been init", "SpeechSynthesize is null");
        } else {
            if (synthesizer.isSpeaking()) synthesizer.pauseSpeaking();
        }
        result.success(null);
    }

    public void resumeSpeaking(MethodCall call, MethodChannel.Result result) {


        if (synthesizer == null) {
            Log.e(TAG, "SpeechSynthesize hasn't been init");
            result.error("", "SpeechSynthesize hasn't been init", "SpeechSynthesize is null");
        } else {
            synthesizer.resumeSpeaking();
        }
        result.success(null);
    }

    public void stopSpeaking(MethodCall call, MethodChannel.Result result) {


        if (synthesizer == null) {
            Log.e(TAG, "SpeechSynthesize hasn't been init");
            result.error("", "SpeechSynthesize hasn't been init", "SpeechSynthesize is null");
        } else {
            synthesizer.stopSpeaking();
        }
        result.success(null);
    }

    private SynthesizerListener mSynthesizerListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            channel.invokeMethod(METHOD_CALL_ONSPEAKBEGIN, null);
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {
            ArrayList<Object> args = new ArrayList<>();
            args.add(i);
            args.add(i1);
            args.add(i2);
            args.add(s);
            channel.invokeMethod(METHOD_CALL_ONBUFFERPROGRESS, args);
        }

        @Override
        public void onSpeakPaused() {
            channel.invokeMethod(METHOD_CALL_ONSPEAKPAUSED, null);
        }

        @Override
        public void onSpeakResumed() {
            channel.invokeMethod(METHOD_CALL_ONSPEAKRESUMED, null);
        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {
            ArrayList<Object> args = new ArrayList<>();
            args.add(i);
            args.add(i1);
            args.add(i2);
            channel.invokeMethod(METHOD_CALL_ONSPEAKPROGRESS, args);
        }

        @Override
        public void onCompleted(SpeechError speechError) {
            channel.invokeMethod(METHOD_CALL_ONCOMPLETED, null);
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };
}
