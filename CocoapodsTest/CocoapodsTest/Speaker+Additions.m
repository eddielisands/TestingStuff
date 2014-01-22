//
//  Speaker+Additions.m
//  CocoapodsTest
//
//  Created by Eddie Li on 21/01/14.
//  Copyright (c) 2014 Eddie Li. All rights reserved.
//

#import "Speaker+Additions.h"

@implementation Speaker (SpeakerAdditions)

+ (Speaker*)createSpeakerInManagedObjectContext:(NSManagedObjectContext*)context {
    return [NSEntityDescription insertNewObjectForEntityForName:@"Speaker"
                                         inManagedObjectContext:context];
}

+ (Speaker*)speakerWithData:(NSDictionary*)speakerData inManagedObjectContext:(NSManagedObjectContext*)context {
    Speaker *speaker = nil;
    NSString *uniqueId = [speakerData objectForKey:@"uniqueId"];
    
    NSError *error = nil;
    
    if (uniqueId) {
        NSFetchRequest *request = [[NSFetchRequest alloc] init];
        request.entity = [NSEntityDescription entityForName:@"Speaker" inManagedObjectContext:context];
        request.predicate = [NSPredicate predicateWithFormat:@"uniqueId = %@", uniqueId];
        
        speaker = [[context executeFetchRequest:request error:&error] lastObject];
    }
    
    if (error) {
        DLog(@"Error getting speaker data from database!");
    }
    
    if (speaker == nil) {
        speaker = [Speaker createSpeakerInManagedObjectContext:context];
    }
    
    speaker.uniqueId = [speakerData objectForKey:@"uniqueId"];
    speaker.displayName = [[speakerData objectForKey:@"displayName"] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    speaker.imageUrl = [speakerData objectForKey:@"image"];
    speaker.company = [[speakerData objectForKey:@"company"] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    
    return speaker;
}

@end