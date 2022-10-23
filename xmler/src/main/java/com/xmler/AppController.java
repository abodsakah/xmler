package com.xmler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

class PersonName {
  String firstName;
  String lastName;
}

class Phone {
  String mobile;
  String home;
}

class Address {
  String street;
  String city;
  String zip;
}

class Family {
  String name;
  String birthYear;
  Address address;
  Phone phone;
}

class Person {
  PersonName name;
  Phone phone;
  Address address;
  ArrayList<Family> family = new ArrayList<Family>();
}

class People {
  ArrayList<Person> person = new ArrayList<Person>();
}

public class AppController {
  @FXML
  private TextField inputFilePath;

  private String inputFilePathString;

  private String fileContent = "";

  @FXML
  private void openFileBrowser() throws IOException {
    // open only txt files
    final FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
    fileChooser.setTitle("Open Resource File");
    inputFilePathString = fileChooser.showOpenDialog(null).getAbsolutePath();

    // set the file path to the text field with fx:id fileInput

    inputFilePath.setText(inputFilePathString);

    readFileData();
  }

  private void readFileData() {
    // open file and read data
    File file = new File(inputFilePathString);

    // read file data
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(file));
      String st;
      while ((st = br.readLine()) != null) {
        fileContent += st + "\n";
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    parseFileData();
  }

  private void parseFileData() {
    // parse file data

    // example of file data
    // P|Carl Gustaf|Bernadotte
    // T|0768-101801|08-101801
    // A|Drottningholms slott|Stockholm|10001
    // F|Victoria|1977
    // A|Haga Slott|Stockholm|10002
    // F|Carl Philip|1979
    // T|0768-101802|08-101802
    // P|Barack|Obama
    // A|1600 Pennsylvania Avenue|Washington, D.C

    // P could be followed of T, A, F
    // F could be followed of A, T

    // P = Person
    // T = Phone
    // A = Address
    // F = Family

    People people = new People();
    Integer personIndex = 0;
    Boolean isPerson = false;
    Boolean isFamily = false;

    for (String line : fileContent.split("\n")) {
      String[] lineData = line.split("\\|");
      String lineType = lineData[0];

      if (lineType.equals("P")) {
        Person person = new Person();
        PersonName personName = new PersonName();
        personName.firstName = lineData[1];
        personName.lastName = lineData[2];
        person.name = personName;
        people.person.add(person);
        personIndex = people.person.indexOf(person);
        isPerson = true;
        isFamily = false;
      } else if (lineType.equals("T")) {
        Phone phone = new Phone();

        if (lineData.length > 1) {
          phone.mobile = lineData[1];
        }
        if (lineData.length > 2) {
          phone.home = lineData[2];
        }

        if (isPerson) {
          people.person.get(personIndex).phone = phone;
        } else if (isFamily) {
          people.person.get(personIndex).family.get(people.person.get(personIndex).family.size() - 1).phone = phone;
        }
      } else if (lineType.equals("A")) {
        Address address = new Address();

        if (lineData.length > 1) {
          address.street = lineData[1];
        }
        if (lineData.length > 2) {
          address.city = lineData[2];
        }
        if (lineData.length > 3) {
          address.zip = lineData[3];
        }

        if (isPerson) {
          people.person.get(personIndex).address = address;
        } else if (isFamily) {
          people.person.get(personIndex).family.get(people.person.get(personIndex).family.size() - 1).address = address;
        }
      } else if (lineType.equals("F")) {
        Family family = new Family();
        family.name = lineData[1];
        family.birthYear = lineData[2];
        people.person.get(personIndex).family.add(family);
        isPerson = false;
        isFamily = true;
      }
    }
    // convert to xml
    convertToXml(people);
  }
  
  private void convertToXml(People people) {
    // convert to xml
    String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    xml += "<people>\n";

    for (Person person : people.person) {
      xml += "\t<person>\n";
      xml += "\t\t<name>\n";
      xml += "\t\t\t<firstName>" + person.name.firstName + "</firstName>\n";
      xml += "\t\t\t<lastName>" + person.name.lastName + "</lastName>\n";
      xml += "\t\t</name>\n";

      if (person.phone != null) {
        xml += "\t\t<phone>\n";
        if (person.phone.mobile != null) {
          xml += "\t\t\t<mobile>" + person.phone.mobile + "</mobile>\n";
        }
        if (person.phone.home != null) {
          xml += "\t\t\t<home>" + person.phone.home + "</home>\n";
        }
        xml += "\t\t</phone>\n";
      }

      if (person.address != null) {
        xml += "\t\t<address>\n";
        if (person.address.street != null) {
          xml += "\t\t\t<street>" + person.address.street + "</street>\n";
        }
        if (person.address.city != null) {
          xml += "\t\t\t<city>" + person.address.city + "</city>\n";
        }
        if (person.address.zip != null) {
          xml += "\t\t\t<zip>" + person.address.zip + "</zip>\n";
        }
        xml += "\t\t</address>\n";
      }

      if (person.family != null) {
        for (Family family : person.family) {
          xml += "\t\t<family>\n";
          xml += "\t\t\t<name>" + family.name + "</name>\n";
          xml += "\t\t\t<born>" + family.birthYear + "</born>\n";

          if (family.phone != null) {
            xml += "\t\t\t<phone>\n";
            if (family.phone.mobile != null) {
              xml += "\t\t\t\t<mobile>" + family.phone.mobile + "</mobile>\n";
            }
            if (family.phone.home != null) {
              xml += "\t\t\t\t<home>" + family.phone.home + "</home>\n";
            }
            xml += "\t\t\t</phone>\n";
          }

          if (family.address != null) {
            xml += "\t\t\t<address>\n";
            if (family.address.street != null) {
              xml += "\t\t\t\t<street>" + family.address.street + "</street>\n";
            }
            if (family.address.city != null) {
              xml += "\t\t\t\t<city>" + family.address.city + "</city>\n";
            }
            if (family.address.zip != null) {
              xml += "\t\t\t\t<zip>" + family.address.zip + "</zip>\n";
            }
            xml += "\t\t\t</address>\n";
          }

          xml += "\t\t</family>\n";
        }
      }
      xml += "\t</person>\n";
    }
    xml += "</people>";


    System.out.println(xml);

  }
}
