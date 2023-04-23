import { Component, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Attraction, Favourites, Restaurant } from '../models/models';
import { AttractionsService } from '../services/attractions.service';
import { FavouritesService } from '../services/favourites.service';
import { RestaurantsService } from '../services/restaurants.service';
import { SearchService } from '../services/search.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-favourites',
  templateUrl: './favourites.component.html',
  styleUrls: ['./favourites.component.css']
})
export class FavouritesComponent implements OnInit {
  form!: FormGroup;
  routeSub$!: Subscription;

  favourites!: Favourites
  // favourites!: FavouritesSearchResult;

  restaurants!: Restaurant[]; 
  shownRestaurants!: Restaurant[]; 

  attractions!: Attraction[]; 
  shownAttractions!: Attraction[]; 

  username: string = this.userService.username; 
  itemsPerPage = 4; 
  currentResIndex: number = 0; 
  currentResPage: number = 1; 
  currentAttrIndex: number = 0; 
  currentAttrPage: number = 1; 
  @ViewChild('paginator') paginator!: MatPaginator;

  constructor(
    private searchService: SearchService,
    private favouritesService: FavouritesService,
    private restaurantsService: RestaurantsService,
    private attractionsService: AttractionsService,
    private userService: UserService,
    private fb: FormBuilder,
    private activatedRoute: ActivatedRoute
  ) {}

  async ngOnInit() {
    this.form = this.createForm();
    this.favourites = await this.getFavourites();
    
    console.info('fave res >>> ', this.restaurants);
    console.info('fave attr >>> ', this.attractions);
  }

  getFavourites = async() => {
    try {
      await this.favouritesService.getFavourites(this.username).then((result) => {
        console.info("fave results >>> ", result)
        this.favourites = result; 

        this.restaurants = this.favourites.restaurants; 
        this.shownRestaurants = this.restaurants.slice(this.currentAttrIndex, this.itemsPerPage);
        this.restaurantsService.restaurants = this.restaurants; 
        
        this.attractions = this.favourites.attractions; 
        this.shownAttractions = this.attractions.slice(this.currentAttrIndex, this.itemsPerPage);
        this.attractionsService.attractions = this.attractions; 
      }); 

    } catch (error) {
      console.info("get favourites error >>>> ", error)
    }

    return this.favourites;
  }

  async removeResFromFavourites(uuid: string) {
    this.favouritesService.deleteFavouriteRestaurant(uuid, this.username); 
    this.favourites = await this.getFavourites();

  }

  async removeAttrFromFavourites(uuid: string) {
    this.favouritesService.deleteFavouriteAttraction(uuid, this.username); 
    this.favourites = await this.getFavourites();
  }

  getResPageDetails(event: any) {
    console.log("event >>> ", event)
    if(event.previousPageIndex < event.pageIndex) this.nextResPage();
    if(event.previousPageIndex > event.pageIndex) this.previousResPage()
  }
  
  nextResPage() {
    this.currentResPage++;
    this.currentResIndex = this.currentResIndex + this.itemsPerPage;
    console.info('currentAttrIndex >>> ', this.currentResIndex);

    console.info("fave res >>> ", this.restaurants);     
    this.shownRestaurants = this.restaurants.slice(this.currentResIndex, (this.currentResIndex + this.itemsPerPage)); 
    console.info("shown res >>> ", this.shownRestaurants); 
  }
  
  previousResPage() {
    this.currentResPage--;
    this.currentResIndex -= this.itemsPerPage;
    console.info("fave res >>> ", this.restaurants); 
    this.shownRestaurants = this.restaurants.slice(this.currentResIndex, (this.currentResIndex + this.itemsPerPage)); 
    console.info("shown res >>> ", this.shownRestaurants); 
  }

  getAttrPageDetails(event: any) {
    console.log("event >>> ", event)
    if(event.previousPageIndex < event.pageIndex) this.nextAttrPage();
    if(event.previousPageIndex > event.pageIndex) this.previousAttrPage();
  }

  nextAttrPage() {
    console.info('currentAttrIndex >>> ', this.currentAttrIndex);
    this.currentAttrPage++;
    this.currentAttrIndex = this.currentAttrIndex + this.itemsPerPage;
    this.shownAttractions = this.attractions.slice(this.currentAttrIndex, (this.currentAttrIndex + this.itemsPerPage)); 
    console.info("shown attr >>> ", this.shownAttractions); 
  }
  
  previousAttrPage() {
    this.currentAttrPage--;
    this.currentAttrIndex -= this.itemsPerPage;
    this.shownAttractions = this.attractions.slice(this.currentAttrIndex, (this.currentAttrIndex + this.itemsPerPage)); 
    console.info("shown attr >>> ", this.shownAttractions); 
  }
  createForm(): FormGroup {
    return this.fb.group({
      keyword: this.fb.control('', [Validators.required]),
    });
  }
}
