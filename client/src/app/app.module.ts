import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HomeComponent } from './components/home.component';
import { RestaurantsComponent } from './components/restaurants.component';
import { AttractionsComponent } from './components/attractions.component';
import { FavouritesComponent } from './components/favourites.component';
import { RestaurantDetailsComponent } from './components/restaurant-details.component';
import { AttractionDetailsComponent } from './components/attraction-details.component';
import { LoginComponent } from './components/login.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    RestaurantsComponent,
    RestaurantDetailsComponent,
    AttractionsComponent,
    AttractionDetailsComponent,
    FavouritesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    ReactiveFormsModule,
    HttpClientModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
