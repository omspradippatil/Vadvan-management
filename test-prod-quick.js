const puppeteer = require('puppeteer');
async function run() {
  const browser = await puppeteer.launch({ headless: 'new', args: ['--no-sandbox'] });
  try {
    const page = await browser.newPage();
    await page.goto('https://vadvan-management.vercel.app/login', { waitUntil: 'domcontentloaded' });
    await page.waitForSelector('input[type="email"]');
    await page.type('input[type="email"]', 'admin@vadhvanport.in');
    await page.type('input[type="password"]', 'Admin@123');
    await page.click('button[type="submit"]');
    
    // Wait up to 10s to see if it reaches dashboard
    await page.waitForFunction('window.location.pathname === "/dashboard"', { timeout: 10000 });
    console.log("SUCCESS");
  } catch (err) {
    console.log("FAILED: " + err.message);
  } finally {
    await browser.close();
  }
}
run();
