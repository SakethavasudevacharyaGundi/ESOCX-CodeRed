"""
ASGI config for legal_aid_backend project.
"""

import os

from django.core.asgi import get_asgi_application

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'legal_aid_backend.settings')

application = get_asgi_application() 