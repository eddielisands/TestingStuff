//
//  HTTPClientManager.m
//  CocoapodsTest
//
//  Created by Eddie Li on 17/01/14.
//  Copyright (c) 2014 Eddie Li. All rights reserved.
//

#import "HTTPClientManager.h"

@implementation HTTPClientManager

- (id)initWithBaseURL:(NSURL *)url {
    self = [super initWithBaseURL:url];
    if (!self) {
        return nil;
    }
    self.responseSerializer = [AFJSONResponseSerializer serializer];
    return self;
}

- (void)httpRequestWithURL:(NSString *)urlString {
    [self GET:urlString
   parameters:nil
      success:^(AFHTTPRequestOperation *operation, id responseObject) {
          if([self.delegate respondsToSelector:@selector(didRequestSuccessWithData:)])
              [self.delegate didRequestSuccessWithData:responseObject];
      }
      failure:^(AFHTTPRequestOperation *operation, NSError *error) {
          if([self.delegate respondsToSelector:@selector(didRequestFailWithError:)])
              [self.delegate didRequestFailWithError:error];
      }];
}

@end
