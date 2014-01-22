//
//  AppConfig.m
//  CocoapodsTest
//
//  Created by Eddie Li on 17/01/14.
//  Copyright (c) 2014 Eddie Li. All rights reserved.
//

#import "AppConfig.h"

BOOL saveDownloadedImageToLocalWithImageName(UIImage* image, NSString* imageName) {
    //Create a folder if it is not existing
    NSString* directoryPath = [kCacheDirectory stringByAppendingPathComponent:DEFAULT_IMAGE_DIRECTORY];
    NSFileManager* fileManager = [NSFileManager defaultManager];
    BOOL isDirectory = YES;
    if (![fileManager fileExistsAtPath:directoryPath isDirectory:&isDirectory]) {
        [fileManager createDirectoryAtPath:directoryPath withIntermediateDirectories:NO attributes:nil error:nil];
    }
    
    NSData* imageData = UIImageJPEGRepresentation(image, 1.0f);
    NSArray* paths = [imageName componentsSeparatedByString:@"/"];
    NSString* fileName;
    NSString* savePath;
    if ([[paths objectAtIndex:(paths.count -1)] length] > 0) {
        fileName = [paths objectAtIndex:(paths.count -1)];
        savePath = [directoryPath stringByAppendingPathComponent:fileName];
        if ([NSKeyedArchiver archiveRootObject:imageData toFile:savePath]) {
            return YES;
        }
    }
    
    return FALSE;
}

UIImage* returnImageFromLocalWithImageName(NSString* imageName) {
    NSFileManager* fileManager = [NSFileManager defaultManager];
    NSString* directoryPath = [kCacheDirectory stringByAppendingPathComponent:DEFAULT_IMAGE_DIRECTORY];

    if ([[imageName substringToIndex:4] isEqualToString:@"http"]) {
        NSArray* paths = [imageName componentsSeparatedByString:@"/"];
        NSString* fileName = [paths objectAtIndex:(paths.count - 1)];
        NSString* filePath = [directoryPath stringByAppendingPathComponent:fileName];
        if ([fileManager fileExistsAtPath:filePath]) {
            NSData* data = [NSKeyedUnarchiver unarchiveObjectWithFile:filePath];
            UIImage* image = [UIImage imageWithData:data];
            return image;
        }
    }
    return nil;
}