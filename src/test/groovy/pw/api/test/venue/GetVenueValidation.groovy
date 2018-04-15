package pw.api.test.venue

import pw.api.test.BaseApiTest
import pw.api.test.helpers.VenueHelper
import pw.api.test.utils.Templater
import spock.lang.*

class GetVenueValidation extends BaseVenueTest {
	
	@Unroll
	def "Get Venue Validation - Get Another User's Custom Venue"() {
		
		given:'A custom venue exists for another user'
			def params = [
				name:'Not accessible to any other user',
				address:'Nowhere Street, Nowherarea, Nowherecity',
				postcode:'XY1 2YZ'
			]
					
		when:'A user to which the venue does not belong gets the venue details'
			def saveResponse = venueHelper.saveVenue(params, individualUserToken)
			def getVenueResponse = venueHelper.getVenue(saveResponse?.id, individualUserToken2)
		
		then:'An appropriate error message is displayed'
			getVenueResponse?.errors[0] == 'You cannot get details for a venue which belongs to another user.'
			getVenueResponse.responseCode == 400
	}
}
