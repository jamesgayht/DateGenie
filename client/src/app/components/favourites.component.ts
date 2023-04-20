import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Favourites } from '../models/models';
import { FavouritesService } from '../services/favourites.service';
import { SearchService } from '../services/search.service';

@Component({
  selector: 'app-favourites',
  templateUrl: './favourites.component.html',
  styleUrls: ['./favourites.component.css']
})
export class FavouritesComponent implements OnInit {
  form!: FormGroup;
  routeSub$!: Subscription;
  favourites!: Favourites

  constructor(
    private searchService: SearchService,
    private favouritesService: FavouritesService,
    private fb: FormBuilder,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.form = this.createForm();
    this.favourites = this.favouritesService.getFavourites();
    console.info('favourites in favourites comp >>> ', this.favourites);
  }

  createForm(): FormGroup {
    return this.fb.group({
      keyword: this.fb.control('', [Validators.required]),
    });
  }
}
