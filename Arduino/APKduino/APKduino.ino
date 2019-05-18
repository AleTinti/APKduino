#include <SoftwareSerial.h>
#include <DHT.h>
#include <LiquidCrystal.h>
#include <Split.h>

SoftwareSerial blue (3, 2);
DHT dht(6, DHT11);
LiquidCrystal lcd (7, 8 , 9, 10, 11, 12);

float h, t;
int i = 0, c = 0, tmax = 27, tmin = 17;
char p[100];


void setup()
{
  Serial.begin(9600);
  dht.begin();
  lcd_start();
  blue.begin(9600);
}

void loop()
{
  delay(500);
  c++;
  if(c%6 == 0)
  {
    htd();
    t = dht.readTemperature();
    h = dht.readHumidity();
    blue.print(String(t) + "/" + String(h));
    if(t > tmax)
    {
      temp_over();
    }
    else if(t < tmin)
    {
      temp_under();
    }
  }
  bluetooth();
}

void bluetooth()
{
  if(blue.available())
  {
    i = 0;
    while(p[i] != 0)
    {
      p[i] = 0;
      i++;
    }
    i = 0;
    while(blue.available())
    {
      p[i] = blue.read();
      i++;
    }
    String str = String(p);
    String maxs = getValue(str,'/',0);
    String mins = getValue(str,'/',1);
    tmax = maxs.toInt();
    tmin = mins.toInt();
    lcd_temp();
    Serial.write("\n");
  }
}

void htd()
{
  t = dht.readTemperature();
  h = dht.readHumidity();
  Serial.print("Temperature: ");
  Serial.print(t);
  Serial.print("°");
  Serial.print("\t\tUmidità: ");
  Serial.print(h);
  Serial.print("%\n");

  lcd.clear();
  lcd.home();
  lcd.print("Temp: ");
  lcd.setCursor(10, 0);
  lcd.print(t);
  lcd.print((char)223);
  lcd.setCursor(0, 1);
  lcd.print("Humidity:");
  lcd.setCursor(10, 1);
  lcd.print(h);
  lcd.print("%");
}

void ht()
{
  t = dht.readTemperature();
  h = dht.readHumidity();
  Serial.print("Temperature: ");
  Serial.print(t);
  Serial.print("°");
  Serial.print("\t\tHumidity: ");
  Serial.print(h);
  Serial.print("%\n");
}

void temp_over()
{
  do {
    lcd.clear();
    lcd.setCursor(2, 0);
    lcd.print("Temperature");
    lcd.setCursor(5, 1);
    lcd.print("High");
    for(i = 0; i < 3; i++)
    {
      lcd.scrollDisplayRight();
      delay(500);
      bluetooth();
      if(t < tmax) loop();
      t = dht.readTemperature();
      ht();
      if(t < tmax) loop();
    }
    for(i = 0; i < 5; i++)
    {
      lcd.scrollDisplayLeft();
      delay(500);
      bluetooth();
      if(t < tmax) loop();
      t = dht.readTemperature();
      ht();
      if(t < tmax) loop();
    }
    for(i = 0; i < 2; i++)
    {
      lcd.scrollDisplayRight();
      delay(500);
      bluetooth();
      if(t < tmax) loop();
      t = dht.readTemperature();
      ht();
      if(t < tmax) loop();
    }
  } while(t > tmax);
}

void temp_under()
{
  do {
    lcd.clear();
    lcd.setCursor(2, 0);
    lcd.print("Temperature");
    lcd.setCursor(6, 1);
    lcd.print("Low");
    for(i = 0; i < 3; i++)
    {
      lcd.scrollDisplayRight();
      delay(500);
      bluetooth();
      if(t > tmin) loop();
      t = dht.readTemperature();
      ht();
      if(t > tmin) loop();
    }
    for(i = 0; i < 5; i++)
    {
      lcd.scrollDisplayLeft();
      delay(500);
      bluetooth();
      if(t > tmin) loop();
      t = dht.readTemperature();
      ht();
      if(t > tmin) loop();
    }
    for(i = 0; i < 2; i++)
    {
      lcd.scrollDisplayRight();
      delay(500);
      bluetooth();
      if(t > tmin) loop();
      t = dht.readTemperature();
      if(t > tmin) loop();
    }
    t = dht.readTemperature();
    ht();
  } while(t < tmin);
}

void lcd_start()
{
  lcd.begin(16, 2);
  lcd.clear();
  lcd.setCursor(2, 0);
  lcd.print("Progetto UDA");
  delay(250);
  lcd.setCursor(0, 1);
  lcd.print("Perucci    Tinti");
  delay(750);
  lcd_temp();
}

void lcd_temp()
{
  lcd.clear();
  lcd.setCursor(2, 0);
  lcd.print("Temperature");
  lcd.setCursor(0, 1);
  lcd.print("Min:");
  lcd.setCursor(4, 1);
  lcd.print(tmin);
  lcd.setCursor(10, 1);
  lcd.print("Max:");
  lcd.setCursor(14, 1);
  lcd.print(tmax);
  delay(1000);
}
