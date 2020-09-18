
import 'interface_tts.dart';
import 'package:iflytts/iflytts.dart';

enum SpeechState {
  stopped,
  playing,
}
class IflyTTS implements InterfaceTTS {
//class IflyTTS {
  Iflytts _tts;
  XFVoiceParam _param;
  XfSpeechListener _speechListener;
  StateChange _stateChange;

//  Synthesizer _tts;
  static IflyTTS _instance;

  factory IflyTTS() => _getInstance();

  static IflyTTS _getInstance() {
    if (null == _instance) {
      _instance = IflyTTS._();
    }
    return _instance;
  }

  IflyTTS._() {
    _tts = Iflytts();
//    _tts = XfSpeechPlugin.instance;
  }

  @override
  Future init() async {
   var res = await _tts.init(appID:'5e168c82');
//   await _tts.setParameter('voice_name', 'xiaoyan');
//    var res = await _tts.initWithAppId(
//        iosAppID: Config.ifly_tts_appid_ios,
//        androidAppID: Config.ifly_tts_appid_android);
    _param = new XFVoiceParam();
//
//    ///domain:应用领域 - iat：日常用语;medical：医疗
//
    _param.domain = 'iat';
    _param.asr_ptt = '0';
    _param.asr_audio_path = 'xme.pcm';

    ///result_type:结果类型,包括：xml, json, plain。xml和json即对应的结构化文本结构，plain即自然语言的文本。
    _param.result_type = 'plain';
//    _param.voice_name = 'vixx';
    _param.voice_name = 'xiaoyan';

    _tts.setParameter(_param.toMap());

    _speechListener = XfSpeechListener(
      onSpeakBegin: () {
//        print('onSpeakBegin');
        _stateChange?.call(SpeechState.playing);
      },
      onCompleted: (Map<dynamic, dynamic> error, String filePath) {
//        print('error:${error},filePath:$filePath');
        _stateChange?.call(SpeechState.stopped);
      },
      onBufferProgress: (int p, int b, int e, String a) {
//        print('onBufferProgress p$p,b$b,e$e,a$a');
      },
      onSpeakProgress: (int p, int b, int e) {
//        print('onSpeakProgress p$p,b$b,e$e');
        if (p > 95) {
          _stateChange?.call(SpeechState.stopped);
        }
      },
      onSpeakPaused: () {
        print('onSpeakPaused');
        _stateChange?.call(SpeechState.stopped);
      },
      onSpeakResumed: () {
        print('onSpeakResumed');
        _stateChange?.call(SpeechState.playing);
      },
    );
    return res;
  }

  @override
  void setStateListener(StateChange change) {
    this._stateChange = change;
  }

  @override
  Future getEngines() async {
    return await null;
  }

  @override
  Future getLanguages() async {
    return await null;
  }

  @override
  Future getVoices() async {
    return await null;
  }

  @override
  Future setLanguage(String language) async {
//    _param.
//    return await _tts.setParameter(_param.toMap());
  }

  ///合成语调:通过此参数，设置合成返回音频的语调，值范围：[0，100]，默认：50
  @override
  Future setPitch(double pitch) async {
//    return await _tts.setParameter(_param.toMap());
  }

  @override
  Future setSilence(int timems) async {
//    return await _tts.setParameter(_param.toMap());
  }

  ///合成语速:通过此参数，设置合成返回音频的语速，值范围：[0，100]，默认：50
  @override
  Future setSpeechRate(double rate) async {
//    return await _tts.setParameter(_param.toMap());
  }

  @override
  Future setVoice(String voice) async {
//    return await _tts.setParameter(_param.toMap());
  }

  ///合成音量:通过此参数，设置合成返回音频的音量，值范围：[0，100]，默认：50
  @override
  Future setVolume(double volume) async {
//    return await _tts.setParameter(_param.toMap());
  }
//  @override
//  Future setLanguage(String language) async {
////    _param.
//    return await _tts.setParameter(_param.toMap());
//  }
//
//  ///合成语调:通过此参数，设置合成返回音频的语调，值范围：[0，100]，默认：50
//  @override
//  Future setPitch(double pitch) async {
//    _param.pitch = pitch?.toString();
//    return await _tts.setParameter(_param.toMap());
//  }
//
//  @override
//  Future setSilence(int timems) async {
//    return await _tts.setParameter(_param.toMap());
//  }
//
//  ///合成语速:通过此参数，设置合成返回音频的语速，值范围：[0，100]，默认：50
//  @override
//  Future setSpeechRate(double rate) async {
//    _param.speed = rate?.toString();
//    return await _tts.setParameter(_param.toMap());
//  }
//
//  @override
//  Future setVoice(String voice) async {
//    _param.voice_name = voice;
//    return await _tts.setParameter(_param.toMap());
//  }
//
//  ///合成音量:通过此参数，设置合成返回音频的音量，值范围：[0，100]，默认：50
//  @override
//  Future setVolume(double volume) async {
//    _param.volume = volume?.toString();
//    return await _tts.setParameter(_param.toMap());
//  }

  @override
  Future speak(String content) async {
    _stateChange?.call(SpeechState.playing);
    var res = await _tts.startSpeaking(string: content,listener: _speechListener);
//    _stateChange?.call(SpeechState.stopped);
    return res;
  }

  @override
  Future stop() async {
    return await _tts.stopSpeaking();
  }
}
