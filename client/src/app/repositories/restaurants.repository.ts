import { Injectable } from "@angular/core";
import Dexie from "dexie";
import { Restaurant } from "../models/models";

@Injectable()
export class RestaurantsRepository extends Dexie {

    restaurants!: Dexie.Table<Restaurant, string>;

    

}