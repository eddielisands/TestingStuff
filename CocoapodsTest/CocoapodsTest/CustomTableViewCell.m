//
//  CustomTableViewCell.m
//  CocoapodsTest
//
//  Created by Eddie Li on 20/01/14.
//  Copyright (c) 2014 Eddie Li. All rights reserved.
//

#import "CustomTableViewCell.h"

@implementation CustomTableViewCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        self.selectionStyle = UITableViewCellSelectionStyleNone;
        
        CustomImageView* speakerImageView = [[CustomImageView alloc] initWithFrame:CGRectMake(10, 10, 80, 80)];
        speakerImageView.tag = 1000;
        speakerImageView.backgroundColor = [UIColor clearColor];
        [speakerImageView imageColorBorder:YES withSize:1.0f/[UIScreen mainScreen].scale withColor:[UIColor lightGrayColor]];
        speakerImageView.layer.masksToBounds = YES;
        speakerImageView.layer.cornerRadius = 10.0f;
        [self.contentView addSubview:speakerImageView];
        
        UILabel* speakerNameLabel = [[UILabel alloc] initWithFrame:CGRectMake(100, 10, 200, 25)];
        speakerNameLabel.tag = 1001;
        speakerNameLabel.backgroundColor = [UIColor clearColor];
        speakerNameLabel.textAlignment = NSTextAlignmentLeft;
        speakerNameLabel.font = [UIFont boldSystemFontOfSize:20];
        speakerNameLabel.textColor = [UIColor blackColor];
        [self.contentView addSubview:speakerNameLabel];
        
        UILabel* speakerCompanyLabel = [[UILabel alloc] initWithFrame:CGRectMake(100, 40, 200, 25)];
        speakerCompanyLabel.tag = 1002;
        speakerCompanyLabel.backgroundColor = [UIColor clearColor];
        speakerCompanyLabel.textAlignment = NSTextAlignmentLeft;
        speakerCompanyLabel.font = [UIFont systemFontOfSize:18];
        speakerCompanyLabel.textColor = [UIColor blackColor];
        [self.contentView addSubview:speakerCompanyLabel];
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
