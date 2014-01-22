//
//  AppManager.h
//  CocoapodsTest
//
//  Created by Eddie Li on 20/01/14.
//  Copyright (c) 2014 Eddie Li. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HTTPClientManager.h"
#import "CoreDataManager.h"

@interface AppManager : NSObject

@property (nonatomic, strong) CoreDataManager* coreDataManager;
@property (nonatomic, strong) HTTPClientManager* httpClientManager;

+ (AppManager* )sharedAppManager;
- (id)initWithApplicationMode;

@end
