package pw.api.test.venue

import pw.api.test.BaseApiTest
import pw.api.test.helpers.VenueHelper
import pw.api.test.utils.Templater
import spock.lang.*

class UpdateVenue extends BaseVenueTest {
	
	@Unroll
	def "Save Venue"() {
		
		given:'A valid custom venue is to be saved'
			def params = [
				id:venueHelper.getRandomCustomVenueId(individualUserToken),
				name:name,
				address:'Nowhere Street, Nowherarea, Nowherecity',
				postcode:postcode
			]
					
		when:'The venue is saved'
			def updateResponse = venueHelper.updateVenue(params, individualUserToken)
			def getVenueResponse = venueHelper.getVenue(updateResponse?.id, individualUserToken)
		
		then:'The venue is correctly saved'
			updateResponse.id != null
			updateResponse.address == params.address
			updateResponse.dateCreated != null && updateResponse.dateCreated != ''
			updateResponse.dateUpdated != null && updateResponse.dateUpdated != ''
			updateResponse.googleMapsPlaceId == null
			updateResponse.isCustomAddress == true
			updateResponse.location == null
			updateResponse.name == params.name
			updateResponse.postcode == params.postcode.toUpperCase()
			updateResponse.user?.id == userHelper.getUserIdByToken(individualUserToken)
			
			getVenueResponse.id == updateResponse.id
			getVenueResponse.address == params.address
			getVenueResponse.dateCreated != null && updateResponse.dateCreated != ''
			getVenueResponse.dateUpdated != null && updateResponse.dateUpdated != ''
			getVenueResponse.googleMapsPlaceId == null
			getVenueResponse.isCustomAddress == true
			getVenueResponse.location == null
			getVenueResponse.name == params.name
			getVenueResponse.postcode == params.postcode.toUpperCase()
			getVenueResponse.user?.id == userHelper.getUserIdByToken(individualUserToken)
		
		where:'The following inputs are used'
			name			   | postcode  | description
			'New Custom Venue' | 'VE1 2NU' | ''
			'Venue with an &'  | 'VE2 3NU' | 'Venue name contains &'
			'Lovercase Pstcde' | 've3 4nu' | 'Lowercase postcode'
	}
}
