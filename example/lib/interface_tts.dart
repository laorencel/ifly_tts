

import 'ifly_tts.dart';

typedef void StateChange(SpeechState state);
abstract class InterfaceTTS {

  Future<dynamic> init();

  void setStateListener(StateChange change);

  Future<dynamic> speak(String content);

  Future<dynamic> stop();

  Future<dynamic> setVoice(String voice);

  Future<dynamic> setLanguage(String language);

  ///range from 0.0 (silent) to 1.0 (loudest)
  Future<dynamic> setVolume(double volume);

  ///1.0 is default and ranges from .5 to 2.0
  Future<dynamic> setPitch(double pitch);

  ///silence period in milliseconds is set according to the parameter
  Future<dynamic> setSilence(int timems);

  ///from 0.0 (slowest) to 1.0 (fastest)
  Future<dynamic> setSpeechRate(double rate);

  Future<dynamic> getLanguages();

  /// [Future] which invokes the platform specific method for getEngines
  /// Returns a list of installed TTS engines
  /// ***Android supported only***
  Future<dynamic> getEngines();

  /// [Future] which invokes the platform specific method for getVoices
  /// Returns a `List` of voice names
  Future<dynamic> getVoices();
}
