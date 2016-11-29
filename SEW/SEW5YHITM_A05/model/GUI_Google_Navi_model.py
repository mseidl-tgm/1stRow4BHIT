import requests
from xml.etree import ElementTree


class Model(object):


    def get_request(location, destination):
        """
        makes a Http request und returns the result
        if wrong inputs the method return "NOT OK"

        :param location: startpoint
        :param destination: endpoint
        :return: string array["directions", "startpoinrt", "endpoint"]
        """
        url = "http://maps.googleapis.com/maps/api/directions/xml"
        params = {"origin": location,
                  "destination": destination,
                  "language": "de"}
        back = ElementTree.fromstring(requests.get(url, params).text)
        if back.find('status').text == 'OK':
            route = back.find('route')
            output = ["", route.find('./leg/start_address').text, route.find('./leg/end_address').text]
            output[0] += "Die Gesamtdauer beträgt: <b>" + route.find(
                "./leg/duration/text").text + " </b>; Die Gesamtentfernung beträgt: <b>" + route.find(
                "./leg/distance/text").text + "</b><br><br>"
            for i in route.iter('step'):
                output[0] += i.find("./html_instructions").text + "; Entfernung: " + i.find(
                    "./distance/text").text + "; Dauer: " + i.find("./duration/text").text + "<br>"
            return output
        else:
            return "NOT OK"