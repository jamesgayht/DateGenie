import { Component } from '@angular/core';
import {
  Attraction,
  AttractionsSearchResults,
  Restaurant,
  RestaurantSearchResults,
} from '../models/models';
import { AttractionsService } from '../services/attractions.service';
import { RestaurantsService } from '../services/restaurants.service';
import { SearchService } from '../services/search.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent {
  constructor(
    private searchService: SearchService,
    private restaurantsService: RestaurantsService,
    private attractionsService: AttractionsService
  ) {}

}