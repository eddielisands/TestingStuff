//
//  CustomImageView.m
//  CocoapodsTest
//
//  Created by Eddie Li on 20/01/14.
//  Copyright (c) 2014 Eddie Li. All rights reserved.
//

#import "CustomImageView.h"

@implementation CustomImageView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        self.contentMode = UIViewContentModeScaleAspectFit;
    }
    return self;
}

/*
 // Only override drawRect: if you perform custom drawing.
 // An empty implementation adversely affects performance during animation.
 - (void)drawRect:(CGRect)rect
 {
 // Drawing code
 }
 */

- (void)imageColorBorder:(BOOL)isApplied withSize:(CGFloat)theSize withColor:(UIColor*)theColor {
    if (isApplied) {
        self.layer.borderWidth = theSize;
        self.layer.borderColor = theColor.CGColor;
    }
}

- (void)setImageURLString:(NSString*)theURLString withPlaceholderImage:(UIImage*)placeholderImage saveToLocal:(BOOL)isSave {
    //Check the image from local
    UIImage* tmpImage = returnImageFromLocalWithImageName(theURLString);
    if (tmpImage) {
        //Set the image directly without animation
        self.image = tmpImage;
    }
    else {
        __weak __typeof(self)weakSelf = self;
        NSURLRequest* request = [NSURLRequest requestWithURL:[NSURL URLWithString:theURLString]];
        
        [self setImageWithURLRequest:request placeholderImage:placeholderImage success:^(NSURLRequest *request, NSHTTPURLResponse *response, UIImage *image) {
            __strong __typeof(weakSelf)strongSelf = weakSelf;
            if (!strongSelf) return;
            if (request && response) {
                //Set the image with animation
                [UIView transitionWithView:strongSelf
                                  duration:0.3f
                                   options:UIViewAnimationOptionTransitionCrossDissolve
                                animations:^{
                                    strongSelf.image = image;
                                }
                                completion:NULL];
            }
            else {
                strongSelf.image = image;
            }
            
            if (isSave) {
                saveDownloadedImageToLocalWithImageName(image, theURLString);
            }
            
            if([strongSelf.delegate respondsToSelector:@selector(downloadImageDidSuccess)])
                [strongSelf.delegate downloadImageDidSuccess];
        } failure:^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error){
            __strong __typeof(weakSelf)strongSelf = weakSelf;
            if([strongSelf.delegate respondsToSelector:@selector(downloadImageDidFailWithError:)])
                [strongSelf.delegate downloadImageDidFailWithError:error];
        }];
    }
}

- (void)setCoreDataObj:(NSManagedObject*)obj withImageData:(NSData*)data withImageURLString:(NSString*)theURLString withPlaceholderImage:(UIImage*)placeholderImage saveToCoreData:(BOOL)isSave {
    if (obj == nil) {
        self.image = placeholderImage;
        return;
    }
    //Check the image from core data
    UIImage* tmpImage = [UIImage imageWithData:data];
    if (tmpImage) {
        //Set the image directly without animation
        self.image = tmpImage;
    }
    else {
        __weak __typeof(self)weakSelf = self;
        NSURLRequest* request = [NSURLRequest requestWithURL:[NSURL URLWithString:theURLString]];
        
        [self setImageWithURLRequest:request placeholderImage:placeholderImage success:^(NSURLRequest *request, NSHTTPURLResponse *response, UIImage *image) {
            __strong __typeof(weakSelf)strongSelf = weakSelf;
            if (!strongSelf) return;
            if (request && response) {
                //Set the image with animation
                [UIView transitionWithView:strongSelf
                                  duration:0.3f
                                   options:UIViewAnimationOptionTransitionCrossDissolve
                                animations:^{
                                    strongSelf.image = image;
                                }
                                completion:NULL];
            }
            else {
                strongSelf.image = image;
            }
            
            if (isSave) {
                if([strongSelf.delegate respondsToSelector:@selector(downloadImageDidSuccessWithImage:withNSManagedObject:)])
                    [strongSelf.delegate downloadImageDidSuccessWithImage:image withNSManagedObject:obj];
            }
            else {
                if([strongSelf.delegate respondsToSelector:@selector(downloadImageDidSuccess)])
                    [strongSelf.delegate downloadImageDidSuccess];
            }
        } failure:^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error){
            __strong __typeof(weakSelf)strongSelf = weakSelf;
            if([strongSelf.delegate respondsToSelector:@selector(downloadImageDidFailWithError:)])
                [strongSelf.delegate downloadImageDidFailWithError:error];
        }];
    }
}

@end
