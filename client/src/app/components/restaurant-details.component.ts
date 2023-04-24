import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Restaurant, Review } from '../models/models';
import { FavouritesService } from '../services/favourites.service';
import { RestaurantsService } from '../services/restaurants.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-restaurant-details',
  templateUrl: './restaurant-details.component.html',
  styleUrls: ['./restaurant-details.component.css'],
})
export class RestaurantDetailsComponent implements OnInit, OnDestroy {
  routeSub$!: Subscription;
  restaurantName!: string;
  restaurant!: Restaurant;

  reviews: Review[] = [];
  shownReviews: Review[] = [];

  gmapURL!: string;
  isLoading: boolean = false;
  username: string = this.userService.username;
  currentIndex: number = 0;
  currentPage = 1;
  reviewsPerPage = 2; 
  @ViewChild('paginator') paginator!: MatPaginator;

  specialChars = [
    '!',
    '@',
    '#',
    '$',
    '%',
    '^',
    '&',
    '*',
    '(',
    ')',
    '-',
    '_',
    '+',
    '=',
    '[',
    ']',
    '{',
    '}',
    '|',
    '\\',
    ';',
    ':',
    "'",
    '"',
    ',',
    '<',
    '.',
    '>',
    '/',
    '?',
  ];

  constructor(
    private activatedRoute: ActivatedRoute,
    private restaurantSvc: RestaurantsService,
    private favouritesService: FavouritesService,
    private userService: UserService,
    private router: Router,
    public sanitizer: DomSanitizer,
    public snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.routeSub$ = this.activatedRoute.params.subscribe((params) => {
      this.restaurantName = params['restaurantName'];
      console.info('restaurant name >>> ', this.restaurantName);

      this.restaurant = this.restaurantSvc.getRestaurantDetailsBytName(
        this.restaurantName
      );
      console.info('restaurant details >>> ', this.restaurant);

      let gmapURLRestaurantName = this.restaurantName.replaceAll(' ', '+');
      for (let specChar of this.specialChars) {
        gmapURLRestaurantName = gmapURLRestaurantName.replaceAll(specChar, '');
      }

      this.gmapURL = `https://www.google.com/maps/embed/v1/place?key=AIzaSyCWijkkimteQ0EbnfYPL2HdVH8zYk0NwPU&q=${gmapURLRestaurantName}&center=${this.restaurant.latitude},${this.restaurant.longitude}`;
      console.info('gmapURL >>> ', this.gmapURL);

      this.reviews = this.restaurant.reviews;
      this.shownReviews = this.reviews.slice(this.currentIndex, this.reviewsPerPage);
      console.info('reviews >>> ', this.reviews);
      this.isLoading = false;
    });
  }

  async addToFavourites(restaurant: Restaurant) {
    console.info('adding to favourites >>> ', restaurant);
    let config = new MatSnackBarConfig();
    config.duration = 3000;
    try {
      await this.favouritesService.updateFavouriteRestaurants(
        restaurant,
        this.username
      );
      const favMessage: string = `Added ${restaurant.name} to favourites!`;
      this.snackBar.open(favMessage, 'Close', config);
    } catch (error) {
      const errorMessage: string = `Failed to add ${restaurant.name} to favourites, item may already exist.`;
      this.snackBar.open(errorMessage, 'Close', config); 
    }
  }

  nextPage() {
    console.info('currentIndex >>> ', this.currentIndex);
    this.currentPage++;
    this.currentIndex = this.currentIndex + this.reviewsPerPage;
    console.info('currentIndex >>> ', this.currentIndex);
    this.shownReviews = this.reviews.slice(this.currentIndex, (this.currentIndex + this.reviewsPerPage)); 
  }

  previousPage() {
    this.currentPage--;
    this.currentIndex -= this.reviewsPerPage;
    this.shownReviews = this.reviews.slice(this.currentIndex, (this.currentIndex + this.reviewsPerPage)); 
  }

  getPageDetails(event: any) {
    console.log("event >>> ", event)
    if(event.previousPageIndex < event.pageIndex) this.nextPage()
    if(event.previousPageIndex > event.pageIndex) this.previousPage()
  }

  ngOnDestroy(): void {
    this.routeSub$.unsubscribe();
  }
}
