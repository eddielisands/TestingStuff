//
//  AppManager.m
//  CocoapodsTest
//
//  Created by Eddie Li on 20/01/14.
//  Copyright (c) 2014 Eddie Li. All rights reserved.
//

#import "AppManager.h"

@implementation AppManager

@synthesize coreDataManager;
@synthesize httpClientManager;

+ (AppManager* )sharedAppManager {
    static dispatch_once_t pred;
    static AppManager* _sharedAppManager = nil;
    
    dispatch_once(&pred, ^{
        _sharedAppManager = [[self alloc] initWithApplicationMode];
    });
    
    return _sharedAppManager;
}

- (id)init {
    self = [super init];
    if (self) {
        NSAssert(NO, @"Sorry, you cannot use [self init] to instance AppManager.");
    }
    return self;
}

- (id)initWithApplicationMode {
    self = [super init];
    if (self) {
        NSString* urlString = @"http://basta.net/2014se/";
        NSURL* url = [NSURL URLWithString:urlString];
        
        self.coreDataManager = [[CoreDataManager alloc] init];
        self.httpClientManager = [[HTTPClientManager alloc] initWithBaseURL:url];
    }
    return self;
}

@end
