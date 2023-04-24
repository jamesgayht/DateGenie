import {
  Component,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Restaurant, RestaurantSearchResults } from '../models/models';
import { FavouritesService } from '../services/favourites.service';
import { RestaurantsService } from '../services/restaurants.service';
import { SearchService } from '../services/search.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-restaurants', 
  templateUrl: './restaurants.component.html',
  styleUrls: ['./restaurants.component.css'],
  host: {
    '(error)': 'updateUrl()',
    '[src]': 'src',
  },
})
export class RestaurantsComponent implements OnInit, OnDestroy {
  keyword!: string;
  currentIndex: number = 0;
  currentPage = 1;
  restaurantsPerPage: number = 8;
  @ViewChild('paginator') paginator!: MatPaginator;
  
  form!: FormGroup;
  restaurants: Restaurant[] = [];
  restaurantsSearchResult!: RestaurantSearchResults;
  totalRecords!: number;
  routeSub$!: Subscription;
  username: string = this.userService.username;

  constructor(
    private searchService: SearchService,
    private restaurantsService: RestaurantsService,
    private favouritesService: FavouritesService,
    private userService: UserService,
    private fb: FormBuilder,
    private activatedRoute: ActivatedRoute,
    public snackBar: MatSnackBar
  ) {}

  async ngOnInit() {
    this.form = this.createForm();
    this.restaurants = await this.searchRestaurants();
    console.info('restaurants in restaurants comp >>> ', this.restaurants);
    this.keyword = this.searchService.getRandomRestaurant(); 
    console.info("username >>>> ", this.username);
  }

  searchRestaurants = async () => {
    if(!!this.paginator) this.paginator.firstPage()
    this.currentIndex = 0; 
    this.currentPage = 1; 
    this.restaurants = [];
    this.keyword = this.form.get('keyword')?.value;
    if (this.keyword === '' || !this.keyword) {
      console.info('search restaurant this.keyword does not exist >>> ', this.keyword);
      this.keyword = '';
    }
    console.info('search restaurant keyword >>> ', this.keyword);
    try {
      await this.searchService.searchRestaurant(this.keyword).then((result) => {
        this.restaurantsSearchResult = result;
        this.restaurants = this.restaurantsSearchResult.restaurants;
        this.totalRecords = this.restaurantsSearchResult.totalRecords;
        console.log('this.restaurants >> ', this.restaurants);
        this.restaurantsService.updateRestaurants(this.restaurants);
      });
    } catch (error) {
      console.log('error >>> ', error);
    }

    return this.restaurants;
  };

  createForm(): FormGroup {
    return this.fb.group({
      keyword: this.fb.control('', [Validators.required]),
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
    this.restaurants = [];
    this.currentPage++;
    this.currentIndex = this.currentIndex + this.restaurantsPerPage;
    console.info('restaurantsPerPage >>> ', this.restaurantsPerPage);
    console.info('currentIndex >>> ', this.currentIndex);
    try {
      this.searchService
        .searchRestaurant(this.keyword, this.currentIndex)
        .then((result) => {
          this.restaurantsSearchResult = result;
          this.restaurants = this.restaurantsSearchResult.restaurants;
          console.log('this.restaurants >> ', this.restaurants);
          this.restaurantsService.updateRestaurants(this.restaurants);
        });
    } catch (error) {
      console.log('error >>> ', error);
    }
  }

  previousPage() {
    this.restaurants = [];
    this.currentPage--;
    this.currentIndex -= this.restaurantsPerPage;
    try {
      this.searchService
        .searchRestaurant(this.keyword, this.currentIndex)
        .then((result) => {
          this.restaurantsSearchResult = result;
          this.restaurants = this.restaurantsSearchResult.restaurants;
          console.log('this.restaurants >> ', this.restaurants);
          this.restaurantsService.updateRestaurants(this.restaurants);
        });
    } catch (error) {
      console.log('error >>> ', error);
    }
  }

  getPageDetails(event: any) {
    console.log("event >>> ", event)
    if(event.previousPageIndex < event.pageIndex) this.nextPage()
    if(event.previousPageIndex > event.pageIndex) this.previousPage()
  }

  ngOnDestroy(): void {
    // this.routeSub$.unsubscribe();
  }
}
