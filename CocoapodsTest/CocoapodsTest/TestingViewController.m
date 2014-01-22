//
//  TestingViewController.m
//  CocoapodsTest
//
//  Created by Eddie Li on 17/01/14.
//  Copyright (c) 2014 Eddie Li. All rights reserved.
//

#import "TestingViewController.h"
#import "CustomTableViewCell.h"
#import "Speaker+Additions.h"

@interface TestingViewController ()

@end

@implementation TestingViewController

@synthesize speakerTableView;
@synthesize array;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        NSMutableArray* tmpArray = [[NSMutableArray alloc] init];
        self.array = tmpArray;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    UIView* tmpView = [[UIView alloc] initWithFrame:CGRectZero];
    UITableView* tmpTableView;
    if (SYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(@"7.0")) {
        tmpTableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 20, [UIScreen mainScreen].applicationFrame.size.width, [UIScreen mainScreen].applicationFrame.size.height) style:UITableViewStylePlain];
    }
    else {
        tmpTableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, [UIScreen mainScreen].applicationFrame.size.width, [UIScreen mainScreen].applicationFrame.size.height) style:UITableViewStylePlain];
    }
    tmpTableView.backgroundColor = [UIColor clearColor];
    tmpTableView.dataSource = self;
    tmpTableView.delegate = self;
    tmpTableView.separatorStyle = UITableViewCellSeparatorStyleSingleLine;
    tmpTableView.separatorColor = [UIColor lightGrayColor];
    tmpTableView.tableHeaderView = tmpView;
    tmpTableView.tableFooterView = tmpView;
    [self.view addSubview:tmpTableView];
    self.speakerTableView = tmpTableView;
    
    NSString* urlString = @"data/conference/all.json";
    [HTTP_CLIENT_MANAGER setDelegate:self];
    [HTTP_CLIENT_MANAGER httpRequestWithURL:urlString];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark -
#pragma mark HTTPClientManagerDelegate Functions
- (void)didRequestSuccessWithData:(id)data {
    __weak __typeof(self)weakSelf = self;
    NSDictionary *jsonDictionary;
    jsonDictionary = (NSDictionary *)data;

    NSArray* speakerArray = [NSArray arrayWithArray:[jsonDictionary objectForKey:@"speakers"]];
    [speakerArray enumerateObjectsUsingBlock:^(id obj, NSUInteger index, BOOL *stop) {
        __strong __typeof(weakSelf)strongSelf = weakSelf;
        NSDictionary* speakerDict = (NSDictionary*)obj;
        Speaker* speaker = [Speaker speakerWithData:speakerDict inManagedObjectContext:[CORE_DATA_MANAGER managedObjectContext]];
        if (speaker) {
            [CORE_DATA_MANAGER setDelegate:strongSelf];
            [CORE_DATA_MANAGER saveContext];
        }
    }];
}

- (void)didRequestFailWithError:(NSError *)error {
    DLog(@"Request Failed. %@", [error localizedDescription]);
}

#pragma mark -
#pragma mark CoreDataManagerDelegate Functions
- (void)didSaveSuccess {
    [CORE_DATA_MANAGER fetchDataFromEntityName:@"Speaker" withSortKey:@"displayName"];
}

- (void)didSaveFailWithError:(NSError *)error {
    DLog(@"Save Failed. %@", [error localizedDescription]);
}

- (void)didFetchSuccessWithData:(NSArray*)data {
    self.array = [NSMutableArray arrayWithArray:data];
    [self.speakerTableView reloadData];
}

- (void)didFetchFailWithError:(NSError*)error {
    DLog(@"Fetch Failed. %@", [error localizedDescription]);
}

#pragma mark -
#pragma mark CustomImageViewDelegate Functions
- (void)downloadImageDidSuccess {
    
}

- (void)downloadImageDidSuccessWithImage:(UIImage*)image withNSManagedObject:(id)obj {
    //Save the image data to core data
    __weak __typeof(id)weakObj = obj;
    __weak __typeof(UIImage*)weakImage = image;
    Speaker* speaker = (Speaker*)weakObj;
    speaker.image = UIImageJPEGRepresentation(weakImage, 1.0f);
}

- (void)downloadImageDidFailWithError:(NSError*)error {
    DLog(@"Download Failed. %@", [error localizedDescription]);
}

#pragma mark -
#pragma mark Table view data source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
    return self.array.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 100.0f;
}

// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    // Configure the cell...
    
    CustomTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[CustomTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    
    CustomImageView* speakerImageView = (CustomImageView*)[cell.contentView viewWithTag:1000];
    UILabel* speakerNameLabel = (UILabel*)[cell.contentView viewWithTag:1001];
    UILabel* speakerCompanyLabel = (UILabel*)[cell.contentView viewWithTag:1002];
    
    Speaker* speaker = [self.array objectAtIndex:indexPath.row];
    
    speakerNameLabel.text = speaker.displayName;
    speakerCompanyLabel.text = speaker.company;
    speakerImageView.image = nil;
    if (speaker.imageUrl.length > 0) {
        speakerImageView.delegate = self;
        //Download image and save to core data
        [speakerImageView setCoreDataObj:speaker withImageData:speaker.image withImageURLString:speaker.imageUrl withPlaceholderImage:nil saveToCoreData:YES];
        //Download image and save to local
//        [speakerImageView setImageURLString:speaker.imageUrl withPlaceholderImage:nil saveToLocal:YES];
    }
    
    return cell;
}

#pragma mark -
#pragma mark Table view delegate
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    // Navigation logic may go here. Create and push another view controller.
    
}

@end
