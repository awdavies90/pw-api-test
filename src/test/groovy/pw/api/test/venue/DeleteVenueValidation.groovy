package pw.api.test.venue

import java.util.concurrent.atomic.AtomicInteger

import pw.api.test.BaseApiTest
import pw.api.test.helpers.VenueHelper
import spock.lang.*

class DeleteVenueValidation extends BaseVenueTest {
	
	def "Delete Venue Validation - No ID Supplied"() {
		
		given:"A venue is to be deleted"
			def venueId = null
					
		when:'The venue is deleted but no id is supplied'
			def response = venueHelper.deleteVenue(venueId, individualUserToken)
		
		then:'An appropriate error response is received'
			response.responseCode == 400
			response.errors[0] == 'id must be of type INT.'
	}
	
	def "Delete Venue Validation - ID Does Not Exist"() {
		
		given:"A venue is to be deleted"
			def venueId = -1
					
		when:'The venue is deleted but the supplied id does not exist'
			def response = venueHelper.deleteVenue(venueId, individualUserToken)
		
		then:'An appropriate error response is received'
			response.responseCode == 400
			response.errors[0] == 'No venue was found with this ID.'
	}
	
	def "Delete Venue Validation - Delete System Venue"() {
		
		given:"A venue is to be deleted"
			def venueId = venueHelper.getRandomVenueId(individualUserToken)
					
		when:'The venue is a non-custom venue'
			def response = venueHelper.deleteVenue(venueId, individualUserToken)
		
		then:'An appropriate error response is received'
			response.responseCode == 400
			response.errors[0] == 'You cannot delete a system venue.'
	}
	
	//----------Permissions Tests----------//
	def "Update Venue Permissions - Update Another User's Custom Address"() {
		
		given:"A custom venue is to be deleted"
			def venueId = venueHelper.getRandomCustomVenueId(individualUserToken)
					
		when:'The venue belongs to another user'
			def response = venueHelper.deleteVenue(venueId, individualUserToken2)
		
		then:'An appropriate error response is received'
			response.responseCode == 400
			response.errors[0] == 'You can not delete a venue which another user has created.'
	}
}
