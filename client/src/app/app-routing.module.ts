
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AttractionDetailsComponent } from './components/attraction-details.component';
import { AttractionsComponent } from './components/attractions.component';
import { FavouritesComponent } from './components/favourites.component';
import { HomeComponent } from './components/home.component';
import { LoginComponent } from './components/login.component';
import { RegisterComponent } from './components/register.component';
import { RestaurantDetailsComponent } from './components/restaurant-details.component';
import { RestaurantsComponent } from './components/restaurants.component';

const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'home', component: HomeComponent},
  {path: 'restaurants', component: RestaurantsComponent},
  {path: 'restaurants/details/:restaurantName', component: RestaurantDetailsComponent},
  {path: 'attractions', component: AttractionsComponent},
  {path: 'attractions/details/:attractionName', component: AttractionDetailsComponent},
  {path: 'favourites', component: FavouritesComponent},
  {path: '**', redirectTo: '/', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
