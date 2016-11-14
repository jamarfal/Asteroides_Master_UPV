package org.example.asteroides.logic;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by jamarfal on 14/11/16.
 */

public class PointsStorageXML_SAX implements PointsStorage {
    private static String FILE = "puntuaciones.xml";
    private Context context;
    private ScoreList scoreList;
    private boolean loadedList;

    public PointsStorageXML_SAX(Context context) {
        this.context = context;
        scoreList = new ScoreList();
        loadedList = false;
    }

    @Override
    public void saveScore(int points, String name, long date) {
        try {
            if (!loadedList) {
                scoreList.readXml(context.openFileInput(FILE));
            }
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }
        scoreList.newScore(points, name, date);
        try {
            scoreList.writeXml(context.openFileOutput(FILE, Context.MODE_PRIVATE));
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }
    }

    @Override
    public Vector<String> scoreList(int amount) {
        try {
            if (!loadedList) {
                scoreList.readXml(context.openFileInput(FILE));
            }
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }
        return scoreList.aVectorString();
    }

    private class ScoreList {

        private class Score {
            int points;
            String name;
            long date;
        }

        private List<Score> scoreList;

        public ScoreList() {
            scoreList = new ArrayList<Score>();
        }

        public void newScore(int points, String name, long date) {
            Score score = new Score();
            score.points = points;
            score.name = name;
            score.date = date;
            scoreList.add(score);
        }

        public Vector<String> aVectorString() {
            Vector<String> result = new Vector<String>();
            for (Score score : scoreList) {
                result.add(score.name + " " + score.points);
            }
            return result;
        }

        public void readXml(InputStream entrada) throws Exception {
            SAXParserFactory fabrica = SAXParserFactory.newInstance();
            SAXParser parser = fabrica.newSAXParser();
            XMLReader lector = parser.getXMLReader();
            XmlHandler xmlHandler = new XmlHandler();
            lector.setContentHandler(xmlHandler);
            lector.parse(new InputSource(entrada));
            loadedList = true;
        }

        public void writeXml(OutputStream salida) {
            XmlSerializer serializador = Xml.newSerializer();
            try {
                serializador.setOutput(salida, "UTF-8");
                serializador.startDocument("UTF-8", true);
                serializador.startTag("", "lista_puntuaciones");
                for (Score score : scoreList) {
                    serializador.startTag("", "score");
                    serializador.attribute("", "date",
                            String.valueOf(score.date));
                    serializador.startTag("", "name");
                    serializador.text(score.name);
                    serializador.endTag("", "name");
                    serializador.startTag("", "points");
                    serializador.text(String.valueOf(score.points));
                    serializador.endTag("", "points");
                    serializador.endTag("", "score");
                }
                serializador.endTag("", "lista_puntuaciones");
                serializador.endDocument();
            } catch (Exception e) {
                Log.e("Asteroides", e.getMessage(), e);
            }
        }

        class XmlHandler extends DefaultHandler {
            private StringBuilder cadena;
            private Score score;

            @Override
            public void startDocument() throws SAXException {
                scoreList = new ArrayList<Score>();
                cadena = new StringBuilder();
            }

            @Override
            public void startElement(String uri, String nombreLocal, String nombreCualif, Attributes atr) throws SAXException {
                cadena.setLength(0);
                if (nombreLocal.equals("score")) {
                    score = new Score();
                    score.date = Long.parseLong(atr.getValue("date"));
                }
            }

            @Override
            public void characters(char ch[], int comienzo, int lon) {
                cadena.append(ch, comienzo, lon);
            }

            @Override
            public void endElement(String uri, String nombreLocal, String nombreCualif) throws SAXException {
                if (nombreLocal.equals("points")) {

                    score.points = Integer.parseInt(cadena.toString());
                } else if (nombreLocal.equals("name")) {
                    score.name = cadena.toString();
                } else if (nombreLocal.equals("score")) {
                    scoreList.add(score);
                }
            }

            @Override
            public void endDocument() throws SAXException {
            }
        }
    }
}