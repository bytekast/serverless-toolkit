import spock.lang.Ignore
import spock.lang.Specification


class FunctionsSpec extends Specification {

  @Ignore
  def 'test process images'() {

    given:
    System.setProperty('aws.profile', 'bytekast')
    def functions = new Functions()
    def event = [Records: [
      [body: 'https://www.cstatic-images.com/car-pictures/xl/USC70CHC021E021001.jpg'],
      [body: 'https://www.ford.com/cmslibs/content/dam/brand_ford/en_us/brand/legacy/nameplate/cars/18_mst_segment_landing_32.jpg/_jcr_content/renditions/cq5dam.web.1280.1280.jpeg'],
    ]]

    when:
    functions.processImages(event, null)

    then:
    noExceptionThrown()
  }


  @Ignore
  def 'test get results'() {

    given:
    System.setProperty('aws.profile', 'bytekast')
    def functions = new Functions()

    when:
    def result = functions.getResults(null)

    then:
    noExceptionThrown()
    println result
  }

}