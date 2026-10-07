const puppeteer = require('puppeteer');

async function runTest() {
  const browser = await puppeteer.launch({ headless: 'new', args: ['--no-sandbox'] });
  const page = await browser.newPage();
  
  page.on('console', msg => {
    console.log(`PAGE LOG [${msg.type()}]: ${msg.text()}`);
  });
  
  page.on('requestfailed', request => {
    console.log(`REQUEST FAILED: ${request.url()} - ${request.failure()?.errorText}`);
  });
  
  page.on('response', response => {
    if (!response.ok()) {
      console.log(`RESPONSE ERROR: ${response.url()} - ${response.status()}`);
    }
  });

  try {
    await page.goto('https://vadvan-management.vercel.app/login', { waitUntil: 'domcontentloaded' });
    await page.waitForSelector('input[type="email"]');
    
    await page.type('input[type="email"]', 'admin@vadhvanport.in');
    await page.type('input[type="password"]', 'Admin@123');
    
    await page.evaluate(() => {
      document.querySelector('button[type="submit"]').click();
    });
    
    await new Promise(r => setTimeout(r, 5000));
    
  } catch (err) {
    console.error(err);
  } finally {
    await browser.close();
  }
}

runTest();
