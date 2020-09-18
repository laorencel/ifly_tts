import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:iflytts/iflytts.dart';

import 'ifly_tts.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    try {
      var res = await IflyTTS().init();
      IflyTTS().setStateListener((state) {
        print(state);
      });
      print(res);
    } on PlatformException {
//      platformVersion = 'Failed to get platform version.';
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
            child: GestureDetector(
          onTap: () {
            IflyTTS().speak('Plugin example app 测试语音');
          },
          child: Container(
            decoration: BoxDecoration(
                color: Colors.lightBlue,
                borderRadius: BorderRadius.circular(8)),
            child: Text('test'),
          ),
        )),
      ),
    );
  }
}
