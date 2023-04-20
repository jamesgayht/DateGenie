import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Restaurant, Review } from '../models/models';
import { RestaurantsService } from '../services/restaurants.service';

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
  gmapURL!: string;
  gmapsApiKey: string = 'AIzaSyCWijkkimteQ0EbnfYPL2HdVH8zYk0NwPU&q';
  isLoading: boolean = false;
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
    private router: Router,
    public sanitizer: DomSanitizer
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
      console.info('reviews >>> ', this.reviews);
      this.isLoading = false; 
    });
  }

  ngOnDestroy(): void {
    this.routeSub$.unsubscribe();
  }
}
