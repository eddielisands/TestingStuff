//
//  ViewController.m
//  Geofening
//
//  Created by KH1386 on 10/8/13.
//  Copyright (c) 2013 KH1386. All rights reserved.
//

#import "ViewController.h"
#import "GeofenceMonitor.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(IBAction)addGeofences:(id)sender
{
    GeofenceMonitor  * gfm = [GeofenceMonitor sharedObj];
    NSMutableDictionary * fence1 = [NSMutableDictionary new];
    [fence1 setValue:@"Fence 1" forKey:@"identifier"];
    [fence1 setValue:@"41.88535" forKey:@"latitude"];
    [fence1 setValue:@"-87.62745" forKey:@"longitude"];
    [fence1 setValue:@"1000" forKey:@"radius"];
    
    NSMutableDictionary * fence2 = [NSMutableDictionary new];
    [fence2 setValue:@"Fence 2" forKey:@"identifier"];
    [fence2 setValue:@"41.92007" forKey:@"latitude"];
    [fence2 setValue:@"-87.63251" forKey:@"longitude"];
    [fence2 setValue:@"100" forKey:@"radius"];
  
    
    NSMutableDictionary * fence3 = [NSMutableDictionary new];
    [fence3 setValue:@"Fence 3" forKey:@"identifier"];
    [fence3 setValue:@"17.224758206624667" forKey:@"latitude"];
    [fence3 setValue:@"78.453369140625" forKey:@"longitude"];
    [fence3 setValue:@"1000" forKey:@"radius"];
    
    if([gfm checkLocationManager])
    {
        [gfm addGeofence:fence1];
        [gfm addGeofence:fence2];
        [gfm addGeofence:fence3];
        [gfm findCurrentFence];
    }

}
-(IBAction)clearGeofences:(id)sender
{
    GeofenceMonitor  * gfm = [GeofenceMonitor sharedObj];

    [gfm clearGeofences];
}


@end
