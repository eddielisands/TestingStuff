//
//  HTTPClientManager.h
//  CocoapodsTest
//
//  Created by Eddie Li on 17/01/14.
//  Copyright (c) 2014 Eddie Li. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <AFNetworking.h>

@protocol HTTPClientManagerDelegate;

@interface HTTPClientManager : AFHTTPRequestOperationManager

@property(weak) id<HTTPClientManagerDelegate> delegate;

- (id)initWithBaseURL:(NSURL*)url;
- (void)httpRequestWithURL:(NSString*)urlString;

@end

@protocol HTTPClientManagerDelegate <NSObject>

- (void)didRequestSuccessWithData:(id)data;
- (void)didRequestFailWithError:(NSError*)error;

@end
