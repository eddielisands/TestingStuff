//
//  Speaker+Additions.h
//  CocoapodsTest
//
//  Created by Eddie Li on 21/01/14.
//  Copyright (c) 2014 Eddie Li. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Speaker.h"

@interface Speaker (SpeakerAdditions)

+ (Speaker*)createSpeakerInManagedObjectContext:(NSManagedObjectContext*)context;
+ (Speaker*)speakerWithData:(NSDictionary*)speakerData inManagedObjectContext:(NSManagedObjectContext*)context;

@end
