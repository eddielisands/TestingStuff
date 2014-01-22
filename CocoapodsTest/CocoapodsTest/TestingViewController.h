//
//  TestingViewController.h
//  CocoapodsTest
//
//  Created by Eddie Li on 17/01/14.
//  Copyright (c) 2014 Eddie Li. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TestingViewController : UIViewController <HTTPClientManagerDelegate, UITableViewDataSource, UITableViewDelegate, HTTPClientManagerDelegate, CoreDataManagerDelegate, CustomImageViewDelegate>

@property (strong, nonatomic) UITableView* speakerTableView;
@property (strong, nonatomic) NSMutableArray* array;

@end
