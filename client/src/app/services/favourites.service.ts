import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';
import {
  Attraction,
  Favourites,
  Restaurant,
} from '../models/models';

@Injectable({
  providedIn: 'root',
})
export class FavouritesService {
  restaurants: Restaurant[] = [];
  attractions: Attraction[] = [];
  favourites: Favourites = new Favourites(this.restaurants, this.attractions);

  constructor(private http: HttpClient) {}

  updateFavouriteRestaurants(restaurant: Restaurant, username: string) {
    this.favourites.restaurants.push(restaurant);

    const body = {
      restaurant_uuid: restaurant.uuid,
      name: restaurant.name,
      type: restaurant.type,
      description: restaurant.description,
      image_uuid: restaurant.imageUUID,
      cuisine: restaurant.cuisine,
      latitude: restaurant.latitude,
      longitude: restaurant.longitude,
      pricing: restaurant.pricing,
      image_url: restaurant.imageURL,
      reviews: restaurant.reviews,
      username: username,
    };

    console.info('body >>> ', body);

    return lastValueFrom(this.http.post('/api/restaurants/favourites', body));
  }

  updateFavouriteAttractions(attraction: Attraction, username: string) {
    this.favourites.attractions.push(attraction);

    const body = {
      attraction_uuid: attraction.uuid,
      name: attraction.name,
      type: attraction.type,
      description: attraction.description,
      image_uuid: attraction.imageUUID,
      latitude: attraction.latitude,
      longitude: attraction.longitude,
      pricing: attraction.pricing,
      image_url: attraction.imageURL,
      reviews: attraction.reviews,
      username: username,
    };

    console.info('body >>> ', body);

    return lastValueFrom(this.http.post('/api/attractions/favourites', body));
  }

  getFavourites(username: string): Promise<Favourites> {
    const params = new HttpParams().set('username', username);

    return lastValueFrom(
      this.http.get<Favourites>(`/api/user/favourites`, {
        params: params,
      })
    );
  }

  deleteFavouriteRestaurant(uuid: string, username: string) {
    const body = {
      uuid: uuid,
      username: username,
    };
    console.info('body >>> ', body);
    return lastValueFrom(this.http.put(`/api/user/restaurant/delete`, body));
  }

  deleteFavouriteAttraction(uuid: string, username: string) {
    const body = {
      uuid: uuid,
      username: username,
    };
    return lastValueFrom(
      this.http.put(`/api/user/attraction/delete`, body)
    );
  }

}
