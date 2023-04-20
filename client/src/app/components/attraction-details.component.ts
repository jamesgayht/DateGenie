import { Component, OnDestroy, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Attraction, Review } from '../models/models';
import { AttractionsService } from '../services/attractions.service';

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
  gmapURL!: string;
  gmapsApiKey: string = 'AIzaSyCWijkkimteQ0EbnfYPL2HdVH8zYk0NwPU&q';
  isLoading: boolean = true;
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
    private router: Router,
    public sanitizer: DomSanitizer
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
      console.info('reviews >>> ', this.reviews);
      this.isLoading = false; 
    });
  }

  ngOnDestroy(): void {
    this.routeSub$.unsubscribe();
  }
}
