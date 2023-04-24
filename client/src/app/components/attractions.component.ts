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
import { Attraction, AttractionsSearchResults } from '../models/models';
import { AttractionsService } from '../services/attractions.service';
import { FavouritesService } from '../services/favourites.service';
import { SearchService } from '../services/search.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-attractions',
  templateUrl: './attractions.component.html',
  styleUrls: ['./attractions.component.css'],
})
export class AttractionsComponent implements OnInit, OnDestroy {
  keyword!: string;
  currentIndex: number = 0;
  currentPage = 1;
  attractionsPerPage: number = 8;
  @ViewChild('paginator') paginator!: MatPaginator;

  form!: FormGroup;
  attractions: Attraction[] = [];
  attractionsSearchResult!: AttractionsSearchResults;
  totalRecords!: number;
  routeSub$!: Subscription;
  username: string = this.userService.username; 

  constructor(
    private searchService: SearchService,
    private attractionsService: AttractionsService,
    private favouritesService: FavouritesService,
    private userService: UserService,
    private fb: FormBuilder,
    private activatedRoute: ActivatedRoute,
    public snackBar: MatSnackBar
  ) {}

  async ngOnInit() {
    this.form = this.createForm();
    this.attractions = await this.searchAttractions();
    console.info('attractions in attractions comp >>> ', this.attractions);
    this.keyword = this.searchService.getRandomAttraction(); 
  }

  searchAttractions = async () => {
    if(!!this.paginator) this.paginator.firstPage()
    this.currentIndex = 0; 
    this.currentPage = 1; 
    this.attractions = []; 
    this.keyword = this.form.get('keyword')?.value;
    if (this.keyword === '' || !this.keyword) {
      console.info(
        'search attraction this.keyword does not exist >>> ',
        this.keyword
      );
      this.keyword = '';
    }
    console.info('search attraction this.keyword >>> ', this.keyword);
    try {
      await this.searchService
        .searchAttractions(this.keyword)
        .then((result) => {
          this.attractionsSearchResult = result;
          this.attractions = this.attractionsSearchResult.attractions;
          this.totalRecords = this.attractionsSearchResult.totalRecords;
          console.log('this.attractions >> ', this.attractions);
          this.attractionsService.updateAttractions(this.attractions);
        });
    } catch (error) {
      console.log('error >>> ', error);
    }

    return this.attractions;
  };

  createForm(): FormGroup {
    return this.fb.group({
      keyword: this.fb.control('', [Validators.required]),
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
    this.attractions = [];
    this.currentPage++;
    this.currentIndex = this.currentIndex + this.attractionsPerPage;
    console.info('attractionsPerPage >>> ', this.attractionsPerPage);
    console.info('currentIndex >>> ', this.currentIndex);
    try {
      this.searchService
        .searchAttractions(this.keyword, this.currentIndex)
        .then((result) => {
          this.attractionsSearchResult = result;
          this.attractions = this.attractionsSearchResult.attractions;
          console.log('this.attractions >> ', this.attractions);
          this.attractionsService.updateAttractions(this.attractions);
        });
    } catch (error) {
      console.log('error >>> ', error);
    }
  }

  previousPage() {
    this.attractions = [];
    this.currentPage--;
    this.currentIndex -= this.attractionsPerPage;
    try {
      this.searchService
        .searchAttractions(this.keyword, this.currentIndex)
        .then((result) => {
          this.attractionsSearchResult = result;
          this.attractions = this.attractionsSearchResult.attractions;
          console.log('this.attractions >> ', this.attractions);
          this.attractionsService.updateAttractions(this.attractions);
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
