#import "IflyttsPlugin.h"
#if __has_include(<iflytts/iflytts-Swift.h>)
#import <iflytts/iflytts-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "iflytts-Swift.h"
#endif

@implementation IflyttsPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftIflyttsPlugin registerWithRegistrar:registrar];
}
@end
