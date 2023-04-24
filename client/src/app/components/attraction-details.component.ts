import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Attraction, Review } from '../models/models';
import { AttractionsService } from '../services/attractions.service';
import { FavouritesService } from '../services/favourites.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-attraction-details',
  templateUrl: './attraction-details.component.html',
  styleUrls: ['./attraction-details.component.css'],
})
export class AttractionDetailsComponent implements OnInit, OnDestroy {
  routeSub$!: Subscription;
  attractionName!: string;
  attraction!: Attraction;

  reviews: Review[] = [];
  shownReviews: Review[] = [];

  gmapURL!: string;
  isLoading: boolean = true;
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
    private attractionSvc: AttractionsService,
    private favouritesService: FavouritesService,
    private userService: UserService,
    private router: Router,
    public sanitizer: DomSanitizer,
    public snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.routeSub$ = this.activatedRoute.params.subscribe((params) => {
      this.attractionName = params['attractionName'];
      console.info('attraction name >>> ', this.attractionName);

      this.attraction = this.attractionSvc.getAttractionDetailsBytName(
        this.attractionName
      );
      console.info('attraction details >>> ', this.attraction);

      let gmapURLAttractionName = this.attractionName.replaceAll(' ', '+');
      for (let specChar of this.specialChars) {
        gmapURLAttractionName = gmapURLAttractionName.replaceAll(specChar, '');
      }

      this.gmapURL = `https://www.google.com/maps/embed/v1/place?key=AIzaSyCWijkkimteQ0EbnfYPL2HdVH8zYk0NwPU&q=${gmapURLAttractionName}&center=${this.attraction.latitude},${this.attraction.longitude}`;
      console.info('gmapURL >>> ', this.gmapURL);
      
      this.reviews = this.attraction.reviews;
      this.shownReviews = this.reviews.slice(this.currentIndex, this.reviewsPerPage);
      console.info('reviews >>> ', this.reviews);
      this.isLoading = false; 
    });
  }

  async addToFavourites(attraction: Attraction) {
    console.info('adding to favourites >>> ', attraction);
    let config = new MatSnackBarConfig();
    config.duration = 3000;
    try {
      await this.favouritesService.updateFavouriteAttractions(
        attraction,
        this.username
      );
      const favMessage: string = `Added ${attraction.name} to favourites!`;
      this.snackBar.open(favMessage, 'Close', config);
    } catch (error) {
      const errorMessage: string = `Failed to add ${attraction.name} to favourites, item may already exist.`;
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
