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
			verifyCustomVenue(params, individualUserToken, updateResponse)
			verifyCustomVenue(params, individualUserToken, getVenueResponse)
		
		where:'The following inputs are used'
			name			   | postcode  | description
			'New Custom Venue' | 'VE1 2NU' | ''
			'Venue with an &'  | 'VE2 3NU' | 'Venue name contains &'
			'Lovercase Pstcde' | 've3 4nu' | 'Lowercase postcode'
	}
	
	def "Update Venue - Same Custom Venue for Two Users"() {
		
		given:'A custom venue is to be saved'
			def params = [
				address:'Nowhere Street, Nowherarea, Nowherecity',
				postcode:'XX1 2XX'
			]
			params.name = 'User 1 Venue'
			def saveResponse1 = venueHelper.saveVenue(params, individualUserToken)
			params.name = 'User 2 Venue'
			def saveResponse2 = venueHelper.saveVenue(params, individualUserToken2)
					
		when:'The same venue is saved for two different users'
			params.name = 'Same Venue Name'
			params.id = saveResponse1?.id
			def updateResponse1 = venueHelper.updateVenue(params, individualUserToken)
			params.id = saveResponse2?.id
			def updateResponse2 = venueHelper.updateVenue(params, individualUserToken2)
		
			def getVenueResponse1 = venueHelper.getVenue(saveResponse1?.id, individualUserToken)
			def getVenueResponse2 = venueHelper.getVenue(saveResponse2?.id, individualUserToken2)
		
		then:'Both venues are correctly saved'
			verifyCustomVenue(params, individualUserToken, updateResponse1)
			verifyCustomVenue(params, individualUserToken, getVenueResponse1)
			verifyCustomVenue(params, individualUserToken2, updateResponse2)
			verifyCustomVenue(params, individualUserToken2, getVenueResponse2)
	}
}
