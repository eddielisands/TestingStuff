//
//  CustomImageView.h
//  CocoapodsTest
//
//  Created by Eddie Li on 20/01/14.
//  Copyright (c) 2014 Eddie Li. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <UIImageView+AFNetworking.h>

@protocol CustomImageViewDelegate;

@interface CustomImageView : UIImageView

@property(weak) id<CustomImageViewDelegate> delegate;

- (void)imageColorBorder:(BOOL)isApplied withSize:(CGFloat)theSize withColor:(UIColor*)theColor;
- (void)setImageURLString:(NSString*)theURLString withPlaceholderImage:(UIImage*)placeholderImage saveToLocal:(BOOL)isSave;
- (void)setCoreDataObj:(NSManagedObject*)obj withImageData:(NSData*)data withImageURLString:(NSString*)theURLString withPlaceholderImage:(UIImage*)placeholderImage saveToCoreData:(BOOL)isSave;

@end

@protocol CustomImageViewDelegate <NSObject>

- (void)downloadImageDidSuccess;
- (void)downloadImageDidSuccessWithImage:(UIImage*)image withNSManagedObject:(id)obj;
- (void)downloadImageDidFailWithError:(NSError*)error;

@end
