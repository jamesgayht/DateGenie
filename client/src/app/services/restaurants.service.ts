import { Injectable } from '@angular/core';
import { Restaurant } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class RestaurantsService {

  constructor() { }

  restaurants: Restaurant[] = [];

  updateRestaurants(restaurants: Restaurant[]) {
    this.restaurants = restaurants;
    console.info("restaurants in service >>> ", this.restaurants)
  }

  getStoredRestaurants() {
    return this.restaurants; 
  }

  getRestaurantDetailsBytName(restaurantName: string) {
    for(let res of this.restaurants) {
      if (res.name === restaurantName) return res; 
    }
    console.info("unable to find restaurant details, returning NA");
    return this.restaurants[0]; 

  }

}
