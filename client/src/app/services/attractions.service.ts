import { Injectable } from '@angular/core';
import { Attraction } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class AttractionsService {

  constructor() { }

  attractions: Attraction[] = [];

  updateAttractions(attractions: Attraction[]) {
    this.attractions = attractions;
    console.info("attractions in service >>> ", this.attractions)
  }

  getStoredAttractions() {
    return this.attractions; 
  }

  getAttractionDetailsBytName(attractionName: string) {
    for(let attraction of this.attractions) {
      if (attraction.name === attractionName) return attraction; 
    }
    console.info("unable to find attraction details, returning NA");
    return this.attractions[0]; 

  }

}
