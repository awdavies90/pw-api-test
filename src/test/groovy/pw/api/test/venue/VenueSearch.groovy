package pw.api.test.venue

import pw.api.test.BaseApiTest
import pw.api.test.helpers.VenueHelper
import pw.api.test.utils.RandomUtils
import spock.lang.*

class VenueSearch extends BaseVenueTest {
	
	@Shared
	static customVenueParams = [
		name:'My Custom Venue',
		address:'Nowhere Street, Nowherarea, Nowherecity',
		postcode:'VE1 2NU',
		street:'Nowhere Street',
		area:'Nowherarea',
		city:'Nowherecity'
	]
	
	def setupSpec() {
		venueHelper.saveVenue(customVenueParams, individualUserToken)
	}
	
	def "Get Venue from Google Maps - By Postcode"() {
		
		given:'A venue is searched for by postcode'
			def postcodes = [
				'NG17 9JQ',
				'ME7 3DD',
				'PR4 6NG',
				'PL20 7RU',
				'TA24 5DL',
				'DY12 1LA'
			]
			def postcode = RandomUtils.getRandomValueFrom(postcodes)
					
		when:'The search is performed'
			def response = venueHelper.venueSearch(postcode, individualUserToken)
		
		then:'Nearby venues are correctly returned'
			verifyVenues(response)
	}
	
	def "Get Venue from Google Maps - By Part Postcode"() {
		
		given:'A venue is searched for by part of its postcode'
			def postcodes = [
				'L30',
				'IP14',
				'LN6',
				'SG18',
				'W1T',
				'SA3'
			]
			def postcode = RandomUtils.getRandomValueFrom(postcodes)
					
		when:'The search is performed'
			def response = venueHelper.venueSearch(postcode, individualUserToken)
		
		then:'Nearby venues are correctly returned'
			verifyVenues(response)
	}
	
	def "Get Venue from Google Maps - By Area"() {
		
		given:'A venue is searched for by area name'
			def areas = [
				'Hucknall',
				'Hopton',
				'Willenhall',
				'Moira',
				'Worsley',
				'Billington'
			]
			def area = RandomUtils.getRandomValueFrom(areas)
					
		when:'The search is performed'
			def response = venueHelper.venueSearch(area, individualUserToken)
		
		then:'Nearby venues are correctly returned'
			verifyVenues(response)
	}
	
	def "Get Venue from Google Maps - By Venue Name and Area"() {
		
		given:'A venue is searched for by venue name and area name'
			def areas = [
				'Belgian Monk, Charing Cross',
				'Plough Inn, Ellington',
				'Middlemore Farm, Daventry',
				'The Prince, Wood Green',
				'Brown Cow, Millom',
				'Crown, Nazeing'
			]
			def area = RandomUtils.getRandomValueFrom(areas)
					
		when:'The search is performed'
			def response = venueHelper.venueSearch(area, individualUserToken)
		
		then:'Nearby venues are correctly returned'
			verifyVenues(response)
	}
	
	def "Get Venue from Google Maps - By City"() {
		
		given:'A venue is searched for by venue name and area name'
			def cities = [
				'Rossendale',
				'Chorley',
				'Bury',
				'Chester',
				'Lisburn',
				'Castleford'
			]
			def city = RandomUtils.getRandomValueFrom(cities)
					
		when:'The search is performed'
			def response = venueHelper.venueSearch(city, individualUserToken)
		
		then:'Nearby venues are correctly returned'
			verifyVenues(response)
	}
	
	def verifyVenues(response) {
		assert response.size() > 0
		assert response.every { it.id != null }
		assert response.every { it.address != null && it.address != '' }
		assert response.every { it.dateCreated != null && it.dateCreated != '' }
		assert response.every { it.dateUpdated != null && it.dateUpdated != '' }
		assert response.every { it.googleMapsPlaceId != null && it.googleMapsPlaceId != '' }
		assert response.every { it.isCustomAddress == false }
		assert response.every { it.location.id != null }
		assert response.every { it.location.lat != null }
		assert response.every { it.location.lng != null }
		assert response.every { it.name != null }
		true
	}
	
	def "Get Custom Venue - By Venue Name"() {
		
		given:'A venue is searched for by venue name'
			def venueName = customVenueParams.name
					
		when:'The search is performed'
			def response = venueHelper.venueSearch(venueName, individualUserToken)
		
		then:'Nearby venues are correctly returned'
			verifyCustomVenues(response)
	}
	
	def "Get Custom Venue - By Postcode"() {
		
		given:'A venue is searched for by postcode'
			def postcode = customVenueParams.postcode
					
		when:'The search is performed'
			def response = venueHelper.venueSearch(postcode, individualUserToken)
		
		then:'Nearby venues are correctly returned'
			verifyCustomVenues(response)
	}
	
	def "Get Custom Venue - By Part Postcode"() {
		
		given:'A venue is searched for by part of a postcode'
			def postcode = customVenueParams.postcode[0..2]
					
		when:'The search is performed'
			def response = venueHelper.venueSearch(postcode, individualUserToken)
		
		then:'Nearby venues are correctly returned'
			verifyCustomVenues(response)
	}
	
	def "Get Custom Venue - By Street"() {
		
		given:'A venue is searched for by street'
			def street = customVenueParams.street
					
		when:'The search is performed'
			def response = venueHelper.venueSearch(street, individualUserToken)
		
		then:'Nearby venues are correctly returned'
			verifyCustomVenues(response)
	}
	
	def "Get Custom Venue - By Area"() {
		
		given:'A venue is searched for by area'
			def area = customVenueParams.area
					
		when:'The search is performed'
			def response = venueHelper.venueSearch(area, individualUserToken)
		
		then:'Nearby venues are correctly returned'
			verifyCustomVenues(response)
	}
	
	def "Get Custom Venue - By City"() {
		
		given:'A venue is searched for by city'
			def city = customVenueParams.city
					
		when:'The search is performed'
			def response = venueHelper.venueSearch(city, individualUserToken)
		
		then:'Nearby venues are correctly returned'
			verifyCustomVenues(response)
	}
	
	def verifyCustomVenues(response) {
		assert response.size() == 1
		assert response[0].id != null
		assert response[0].address == customVenueParams.address
		assert response[0].dateCreated != null && response[0].dateCreated != ''
		assert response[0].dateUpdated != null && response[0].dateUpdated != ''
		assert response[0].googleMapsPlaceId == null
		assert response[0].isCustomAddress == true
		assert response[0].location == null
		assert response[0].name == customVenueParams.name
		assert response[0].postcode == customVenueParams.postcode
		assert response[0].user?.id == userHelper.getUserIdByToken(individualUserToken)
		true
	}
}
