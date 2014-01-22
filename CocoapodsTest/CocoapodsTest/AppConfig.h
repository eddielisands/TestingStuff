//
//  AppConfig.h
//  CocoapodsTest
//
//  Created by Eddie Li on 17/01/14.
//  Copyright (c) 2014 Eddie Li. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "AppDelegate.h"
#import "AppManager.h"

#define DEFAULT_IMAGE_DIRECTORY @"ImageFolder"

// Get AppDelegate
#define APP_DELEGATE (AppDelegate *)[[UIApplication sharedApplication] delegate]

// Get Managers
#define CORE_DATA_MANAGER (CoreDataManager* )[[AppManager sharedAppManager] coreDataManager]
#define HTTP_CLIENT_MANAGER (HTTPClientManager *)[[AppManager sharedAppManager] httpClientManager]

// Return the Document Directory Path
#define kDocumentDirectory [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0]

// Return the Cache Directory Path
#define kCacheDirectory [NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES) objectAtIndex:0]

// For Easy Prompt Alert View
#define ALERT(title,msg,buttonTitle){UIAlertView* alert = [[UIAlertView alloc] initWithTitle:title message:msg delegate:nil cancelButtonTitle:buttonTitle otherButtonTitles:nil];[alert show];[alert release];}

// For System Version Checking
#define SYSTEM_VERSION_EQUAL_TO(v)                  ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] == NSOrderedSame)
#define SYSTEM_VERSION_GREATER_THAN(v)              ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] == NSOrderedDescending)
#define SYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(v)  ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] != NSOrderedAscending)
#define SYSTEM_VERSION_LESS_THAN(v)                 ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] == NSOrderedAscending)
#define SYSTEM_VERSION_LESS_THAN_OR_EQUAL_TO(v)     ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] != NSOrderedDescending)

#pragma mark - Debug Support
#ifdef DEBUG
#define DLog(...) NSLog(@"%s %@", __PRETTY_FUNCTION__, [NSString stringWithFormat:__VA_ARGS__])
#else
#define DLog(...) do {  } while (0)
#endif

BOOL saveDownloadedImageToLocalWithImageName(UIImage* image, NSString* imageName);
UIImage* returnImageFromLocalWithImageName(NSString* imageName);
