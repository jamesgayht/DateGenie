import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { param } from 'jquery';
import { firstValueFrom, lastValueFrom } from 'rxjs';
import {
  AttractionsSearchResults,
  Restaurant,
  RestaurantSearchResults,
} from '../models/models';

@Injectable({
  providedIn: 'root',
})
export class SearchService {
  constructor(private http: HttpClient) {}

  searchRestaurant(
    keyword?: string,
    offset?: number
  ): Promise<RestaurantSearchResults> {
    if (!keyword || keyword === '') keyword = this.randomCuisine;
    if (!offset || offset === 0) offset = 0;
    console.info('keyword >>> ', keyword);
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('offset', offset);
    return lastValueFrom(
      this.http.get<RestaurantSearchResults>(`/api/restaurants/${keyword}`, {
        params: params,
      })
    );
  }

  searchAttractions(
    keyword?: string,
    offset?: number
  ): Promise<AttractionsSearchResults> {
    if (!keyword || keyword === '') keyword = this.randomAttraction;
    if (!offset || offset === 0) offset = 0;
    console.info('keyword >>> ', keyword);
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('offset', offset);
    return lastValueFrom(
      this.http.get<AttractionsSearchResults>(`/api/attractions/${keyword}`, {
        params: params,
      })
    );
  }

  randomizer(array: any[]) {
    const randomNum = Math.floor(Math.random() * array.length);
    return array[randomNum];
  }

  getRandomAttraction() {
    return this.randomAttraction;
  }

  getRandomRestaurant() {
    return this.randomCuisine;
  }

  restaurantCuisines: string[] = [
    'Chinese',
    'Malay',
    'Indian',
    'Japanese',
    'Thai',
    'Vietnamese',
    'Indonesian',
    'Western',
    'Italian',
    'Halal',
  ];
  randomCuisine: string = this.randomizer(this.restaurantCuisines);

  attractionsThemes: string[] = [
    'Sports',
    'Wildlife',
    'Historical',
    'Art',
    'Food',
  ];
  randomAttraction: string = this.randomizer(this.attractionsThemes);
}
