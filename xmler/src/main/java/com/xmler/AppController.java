package com.xmler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
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
  @FXML
  private TextArea xmlDisplay;

  private String inputFilePathString;

  private String fileContent = "";

  private String xml = "";

  @FXML
  private void openFileBrowser() throws IOException {
    // open only txt files
    final FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
    fileChooser.setTitle("Open Resource File");
    inputFilePathString = fileChooser.showOpenDialog(null).getAbsolutePath();

    inputFilePath.setText(inputFilePathString);

    readFileData();
  }

  private void readFileData() {
    File file = new File(inputFilePathString);

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
    xml += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
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

    // write to textArea
    xmlDisplay.setText(xml);
  }

  @FXML
  private void saveAs() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Specify a file to save");

    FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files", "xml");
    fileChooser.setFileFilter(filter);

    int userSelection = fileChooser.showSaveDialog(fileChooser);

    // save file as xml
    if (userSelection == JFileChooser.APPROVE_OPTION) {
      File fileToSave = fileChooser.getSelectedFile();

      // check if there is a file extension
      if (!fileToSave.getName().contains(".")) {
        fileToSave = new File(fileToSave.getAbsolutePath() + ".xml");
      }

      // check if file exists
      if (fileToSave.exists()) {
        int response = JOptionPane.showConfirmDialog(null, "Overwrite existing file?", "Confirm Overwrite",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.CANCEL_OPTION) {
          return;
        }
      }

      try {
        FileWriter fileWriter = new FileWriter(fileToSave);
        fileWriter.write(xml);
        fileWriter.close();
      } catch (IOException ex) {
        System.err.println(ex);
      }
    }
  }

  @FXML
  private void save() {
    // get file name and remove extension and add .xml
    String fileName = inputFilePathString.substring(0, inputFilePathString.lastIndexOf(".")) + ".xml";

    // check if file exists
    File fileToSave = new File(fileName);
    if (fileToSave.exists()) {
      int response = JOptionPane.showConfirmDialog(null, "Overwrite existing file?", "Confirm Overwrite",
          JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
      if (response == JOptionPane.CANCEL_OPTION) {
        return;
      }
    }

    try {
      FileWriter fileWriter = new FileWriter(fileToSave);
      fileWriter.write(xml);
      fileWriter.close();
    } catch (IOException ex) {
      System.err.println(ex);
    }
  }
}
