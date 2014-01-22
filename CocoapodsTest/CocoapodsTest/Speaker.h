//
//  Speaker.h
//  CocoapodsTest
//
//  Created by Eddie Li on 21/01/14.
//  Copyright (c) 2014 Eddie Li. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Speaker : NSManagedObject

@property (nonatomic, retain) NSString * displayName;
@property (nonatomic, retain) NSString * company;
@property (nonatomic, retain) NSString * imageUrl;
@property (nonatomic, retain) NSData * image;
@property (nonatomic, retain) NSString * uniqueId;

@end
