//
//  CoreDataManager.h
//  CocoapodsTest
//
//  Created by Eddie Li on 20/01/14.
//  Copyright (c) 2014 Eddie Li. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@protocol CoreDataManagerDelegate;

@interface CoreDataManager : NSObject

@property(weak) id<CoreDataManagerDelegate> delegate;

@property (nonatomic, strong, readonly) NSManagedObjectModel* managedObjectModel;
@property (nonatomic, strong, readonly) NSManagedObjectContext* managedObjectContext;
@property (nonatomic, strong, readonly) NSPersistentStoreCoordinator* persistentStoreCoordinator;

- (void)saveContext;
- (void)fetchDataFromEntityName:(NSString*)name withSortKey:(NSString*)key;

@end

@protocol CoreDataManagerDelegate <NSObject>

- (void)didSaveSuccess;
- (void)didSaveFailWithError:(NSError*)error;

- (void)didFetchSuccessWithData:(NSArray*)data;
- (void)didFetchFailWithError:(NSError*)error;

@end

