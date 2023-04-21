export interface UserSearchResult {
    resp: string
}

export interface Restaurant {
	uuid: string
    name: string
	type: string
	description: string
    imageUUID: string
    cuisine: string
    reviews: Review[]
    latitude: number
    longitude: number
	imageURL: string
    pricing: string
}

export interface RestaurantSearchResults {
    restaurants: Restaurant[]
    totalRecords: number
    date: string 
}

export interface Attraction {
	uuid: string
    name: string
	type: string
	description: string
    imageUUID: string
    reviews: Review[]
    latitude: number
    longitude: number
	imageURL: string
    pricing: string
}

export interface AttractionsSearchResults {
    attractions: Attraction[]
    totalRecords: number
    date: string 
}

export interface Review {
    authorName: string
    rating: number 
    text: string
    time: string
}

export class User {
    constructor(username: string, email: string, password: string) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    username: string
    email: string
    password: string
}

export class Favourites {
    constructor(restaurants: Restaurant[], attractions: Attraction[]) {
        this.restaurants = restaurants;
        this.attractions = attractions;
    }
    restaurants: Restaurant[] = []
    attractions: Attraction[] = []
}