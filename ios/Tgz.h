
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNTgzSpec.h"

@interface Tgz : NSObject <NativeTgzSpec>
#else
#import <React/RCTBridgeModule.h>

@interface Tgz : NSObject <RCTBridgeModule>
#endif

@end
