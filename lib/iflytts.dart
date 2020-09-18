import 'dart:async';

import 'package:flutter/services.dart';

class Iflytts {
  static const MethodChannel _channel =
      const MethodChannel('iflytts');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
